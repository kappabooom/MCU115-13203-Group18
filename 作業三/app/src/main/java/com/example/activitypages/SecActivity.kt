package com.example.activitypages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity : AppCompatActivity() {

    companion object {
        const val KEY_INPUT = "input_key"
        const val KEY_REPLY = "reply_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sec)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvReceived = findViewById<TextView>(R.id.tvReceived)
        val etReply = findViewById<EditText>(R.id.etReply)
        val btnReturn = findViewById<Button>(R.id.btnReturn)

        // 從 Intent 的 Bundle 取出第一頁傳來的文字
        val receivedText = intent.extras?.getString(KEY_INPUT)
        if (!receivedText.isNullOrEmpty()) {
            tvReceived.text = receivedText
        }

        btnReturn.setOnClickListener {
            val bundle = Bundle().apply {
                putString(KEY_REPLY, etReply.text.toString())
            }
            val resultIntent = Intent().apply { putExtras(bundle) }
            // STEP 03: 使用 setResult() 儲存要回傳的資料
            setResult(RESULT_OK, resultIntent)
            // STEP 04: 使用 finish() 結束 SecActivity 並回到 MainActivity
            finish()
        }
    }
}
