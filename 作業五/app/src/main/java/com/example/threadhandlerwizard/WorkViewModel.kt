package com.example.threadhandlerwizard

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkViewModel : ViewModel() {
    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    @Volatile private var running = false

    fun start() {
        if (running) return
        running = true
        _status.postValue("Preparing…")
        _progress.postValue(0)
        worker.post {
            Thread.interrupted() // 清掉上一次 cancel 可能留下的中斷旗標
            try {
                Thread.sleep(10000) // discovery，準備工作放這裏
                if (!running) throw InterruptedException()
                _status.postValue("Working…")
                for (i in 1..100) {
                    if (!running) break
                    Thread.sleep(3500) // 真正的背景工作放這裏
                    _progress.postValue(i)
                }
                _status.postValue(if (running) "背景工作結束！" else "Canceled")
                running = false
            } catch (_: InterruptedException) {
                _status.postValue("Canceled")
                running = false
            }
        }
    }

    fun cancel() {
        if (!running) return
        running = false
        handlerThread.interrupt() // 立即喚醒 sleep 中的背景執行緒，中斷工作
    }

    override fun onCleared() {
        running = false
        handlerThread.interrupt()
        handlerThread.quitSafely()
        super.onCleared()
    }
}
