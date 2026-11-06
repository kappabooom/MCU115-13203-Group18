# MyCalculator（第二週作業）

用 Kotlin + XML 版面做的 Android 計算機。

## 功能
- 數字 0–9、小數點
- 運算符 + − × ÷（可連續運算，例如 `2 + 3 × 3 = 15`）
- `=` 計算結果，上方綠色小字顯示算式（例如 `1831×2`），下方大字顯示結果
- `C` 清除目前輸入、`AC` 全部清除、⌫ 刪除一位
- 除以 0 顯示 `Error`

## 檔案
- `app/src/main/res/layout/activity_main.xml`：計算機介面（2 個 TextView + 按鈕）
- `app/src/main/java/com/example/mycalculator/MainActivity.kt`：計算機邏輯與按鈕監聽器
