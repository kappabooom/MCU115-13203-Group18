package com.example.threadhandlerwizard

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 作業五 + 作業六
 * - 背景工作在 HandlerThread 上執行
 * - 背景工作開始時播放 background_music.mp3，完成或中斷時停止音樂
 *
 * MediaPlayer 一律只在 main thread 操作（start / stop / release），
 * 這樣按下 Cancel 時音樂能「立刻」停止，而不用排在背景工作後面等待。
 */
class WorkViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private const val PREPARE_MS = 10_000L   // 準備時間
        private const val STEP_MS = 3_500L       // 每 1% 的工作時間
        private const val CHECK_MS = 100L        // 每 100ms 檢查一次是否被取消
    }

    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)
    private val main = Handler(Looper.getMainLooper())

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    // 只在 main thread 存取
    private var mediaPlayer: MediaPlayer? = null

    @Volatile private var running = false
    // 每次按 Start 都會產生新的 jobId；舊的工作若發現 jobId 不同就會自己結束
    @Volatile private var jobId = 0

    /** 由 UI (main thread) 呼叫 */
    fun start() {
        if (running) return
        running = true
        val job = ++jobId

        _status.value = "Preparing…"
        _progress.value = 0

        // 背景工作開始 → 播放音樂
        startMusic()

        worker.post {
            // 準備工作
            if (!waitWhileActive(PREPARE_MS, job)) return@post
            updateIfActive(job) { _status.value = "Working…" }

            // 真正的背景工作
            for (i in 1..100) {
                if (!waitWhileActive(STEP_MS, job)) return@post
                updateIfActive(job) { _progress.value = i }
            }

            // 背景工作結束 → 停止音樂
            updateIfActive(job) {
                running = false
                _status.value = "背景工作結束！"
                stopMusic()
            }
        }
    }

    /** 由 UI (main thread) 呼叫 */
    fun cancel() {
        if (!running) return
        running = false
        jobId++                 // 讓正在跑的背景工作失效
        _status.value = "Canceled"
        // 中斷 → 立刻停止音樂
        stopMusic()
    }

    override fun onCleared() {
        running = false
        jobId++
        stopMusic()
        main.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
        super.onCleared()
    }

    // ---------------- helpers ----------------

    private fun isActive(job: Int) = running && jobId == job

    /** 分段 sleep，讓 Cancel 可以在 100ms 內生效（原本 sleep 10 秒中無法中斷） */
    private fun waitWhileActive(totalMs: Long, job: Int): Boolean {
        var left = totalMs
        try {
            while (left > 0) {
                if (!isActive(job)) return false
                val s = minOf(left, CHECK_MS)
                Thread.sleep(s)
                left -= s
            }
        } catch (_: InterruptedException) {
            return false
        }
        return isActive(job)
    }

    /** 切回 main thread 更新 UI；若工作已被取消就不更新（避免 Canceled 被蓋掉） */
    private fun updateIfActive(job: Int, block: () -> Unit) {
        main.post { if (isActive(job)) block() }
    }

    private fun startMusic() {
        stopMusic() // 確保不會同時有兩個 MediaPlayer
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.background_music)?.apply {
            isLooping = true
            start()
        }
    }

    private fun stopMusic() {
        mediaPlayer?.let { mp ->
            try {
                if (mp.isPlaying) mp.stop()
            } catch (_: IllegalStateException) {
            }
            mp.release()
        }
        mediaPlayer = null
    }
}
