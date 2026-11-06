package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    // ===== 第三步：UI 元件變數 =====
    private lateinit var tvExpression: TextView   // 算式，例如 1831×2
    private lateinit var tvOperator: TextView     // 目前的運算符
    private lateinit var tvResult: TextView       // 目前輸入 / 計算結果

    // ===== 計算機狀態 =====
    private var currentInput = ""                 // 正在輸入的數字
    private var firstOperand: BigDecimal? = null  // 第一個運算元
    private var pendingOperator: String? = null   // 等待計算的運算符
    private var justCalculated = false            // 剛按過「=」
    private var lastOperator = ""                 // 上一次計算用的運算符（按 = 後仍顯示）

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvOperator = findViewById(R.id.tvOperator)
        tvResult = findViewById(R.id.tvResult)

        // ===== 第五步：設定按鈕監聽器 =====
        val digitButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        for (id in digitButtons) {
            val button = findViewById<Button>(id)
            button.setOnClickListener { appendDigit(button.text.toString()) }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { appendDot() }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("÷") }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearEntry() }
        findViewById<Button>(R.id.btnAllClear).setOnClickListener { clearAll() }
        findViewById<ImageButton>(R.id.btnBackspace).setOnClickListener { backspace() }

        updateDisplay()
    }

    // ---------- 附加數字 ----------
    private fun appendDigit(digit: String) {
        if (justCalculated) {          // 算完後直接輸入數字 = 開始新的計算
            clearAll()
        }
        currentInput = when {
            currentInput == "0" -> digit           // 避免 007 這種輸入
            currentInput.length >= MAX_DIGITS -> currentInput
            else -> currentInput + digit
        }
        updateDisplay()
    }

    private fun appendDot() {
        if (justCalculated) {
            clearAll()
        }
        if (!currentInput.contains(".")) {
            currentInput = if (currentInput.isEmpty()) "0." else "$currentInput."
        }
        updateDisplay()
    }

    // ---------- 設定運算符 ----------
    private fun setOperator(operator: String) {
        if (currentInput == ERROR) clearAll()

        if (currentInput.isNotEmpty()) {
            val value = currentInput.toBigDecimal()
            val first = firstOperand
            val op = pendingOperator
            // 連續運算：例如 2 + 3 × → 先算出 5 再接 ×
            firstOperand = if (first != null && op != null) {
                compute(first, value, op) ?: return showError()
            } else {
                value
            }
            currentInput = ""
        } else if (firstOperand == null) {
            firstOperand = BigDecimal.ZERO     // 一開始就按運算符，視為 0
        }
        // currentInput 為空且已有 firstOperand：只是換運算符

        pendingOperator = operator
        justCalculated = false
        tvExpression.text = format(firstOperand!!) + operator
        updateDisplay()
    }

    // ---------- 計算結果（=） ----------
    private fun calculateResult() {
        val first = firstOperand
        val op = pendingOperator
        if (first == null || op == null || currentInput.isEmpty()) return

        val second = currentInput.toBigDecimal()
        val result = compute(first, second, op) ?: return showError()

        tvExpression.text = format(first) + op + format(second)
        currentInput = format(result)
        firstOperand = null
        pendingOperator = null
        lastOperator = op
        justCalculated = true
        updateDisplay()
    }

    /** 第四步：用 when 判斷運算符並計算；除以 0 回傳 null */
    private fun compute(a: BigDecimal, b: BigDecimal, operator: String): BigDecimal? =
        when (operator) {
            "+" -> a.add(b)
            "-" -> a.subtract(b)
            "×" -> a.multiply(b)
            "÷" -> if (b.signum() == 0) null else a.divide(b, MathContext.DECIMAL64)
            else -> null
        }

    // ---------- 清除輸入 ----------
    /** C：只清掉目前輸入的數字 */
    private fun clearEntry() {
        if (justCalculated || currentInput == ERROR) {
            clearAll()
            return
        }
        currentInput = ""
        updateDisplay()
    }

    /** AC：全部清除 */
    private fun clearAll() {
        currentInput = ""
        firstOperand = null
        pendingOperator = null
        justCalculated = false
        lastOperator = ""
        tvExpression.text = ""
        updateDisplay()
    }

    /** ⌫：刪除最後一位 */
    private fun backspace() {
        if (justCalculated || currentInput == ERROR) {
            clearAll()
            return
        }
        currentInput = currentInput.dropLast(1)
        if (currentInput == "-") currentInput = ""
        updateDisplay()
    }

    private fun showError() {
        clearAll()
        currentInput = ERROR
        justCalculated = true
        updateDisplay()
    }

    // ---------- 更新顯示內容 ----------
    private fun updateDisplay() {
        tvResult.text = currentInput.ifEmpty { "0" }
        tvOperator.text = pendingOperator ?: if (justCalculated) lastOperator else ""
    }

    /** 把 BigDecimal 轉成好讀的字串：3662.0 → 3662、0.5000 → 0.5 */
    private fun format(value: BigDecimal): String {
        val rounded = value.setScale(MAX_DECIMALS, RoundingMode.HALF_UP).stripTrailingZeros()
        return if (rounded.signum() == 0) "0" else rounded.toPlainString()
    }

    companion object {
        private const val MAX_DIGITS = 15
        private const val MAX_DECIMALS = 10
        private const val ERROR = "Error"
    }
}
