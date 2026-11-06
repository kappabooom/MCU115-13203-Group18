package com.example.activitypages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var etInput: EditText

    // STEP 01: 宣告 ActivityResultLauncher 作為 Activity 啟動器
    // STEP 05: 在回呼中取得 SecActivity 回傳的資料
    private val startForResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val replyText = result.data?.extras?.getString(SecActivity.KEY_REPLY)
            if (!replyText.isNullOrEmpty()) {
                tvResult.text = replyText
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etInput = findViewById(R.id.etInput)
        tvResult = findViewById(R.id.tvResult)
        val btnSwitch = findViewById<Button>(R.id.btnSwitch)

        // STEP 02: 使用 Intent + Bundle 夾帶資料，透過 ActivityResultLauncher 前往 SecActivity
        btnSwitch.setOnClickListener {
            val bundle = Bundle().apply {
                putString(SecActivity.KEY_INPUT, etInput.text.toString())
            }
            val intent = Intent(this, SecActivity::class.java).apply {
                putExtras(bundle)
            }
            startForResult.launch(intent)
        }
    }
}
