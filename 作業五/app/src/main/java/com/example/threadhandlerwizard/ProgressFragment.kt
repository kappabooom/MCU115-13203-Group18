package com.example.threadhandlerwizard

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bar = view.findViewById<ProgressBar>(R.id.progressBar)
        val percent = view.findViewById<TextView>(R.id.txtPercent) // 圓環中間的 %
        val text = view.findViewById<TextView>(R.id.txt)
        bar.max = 100

        vm.status.observe(viewLifecycleOwner) { s ->
            when {
                s.startsWith("Preparing") -> {
                    // 準備中：圓環轉圈（不確定進度）
                    bar.isIndeterminate = true
                    percent.text = ""
                    text.text = s
                }
                s.startsWith("Working") -> {
                    bar.isIndeterminate = false
                    val p = vm.progress.value ?: 0
                    bar.progress = p
                    percent.text = "%d%%".format(p)
                    text.text = "Working… %d%%".format(p)
                }
                else -> {
                    // Idle / Canceled / 背景工作結束！
                    bar.isIndeterminate = false
                    bar.progress = vm.progress.value ?: 0
                    percent.text = "%d%%".format(bar.progress)
                    text.text = s
                }
            }
        }
        vm.progress.observe(viewLifecycleOwner) { p ->
            if (!bar.isIndeterminate) {
                bar.progress = p
                percent.text = "%d%%".format(p)
                if (vm.status.value?.startsWith("Working") == true) {
                    text.text = "Working… %d%%".format(p)
                }
            }
        }
    }
}
