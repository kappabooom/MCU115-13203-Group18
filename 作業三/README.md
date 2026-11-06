# ActivityPages（第三週作業：Page Switching 頁面切換與雙向傳值）

兩個 Activity 之間切換頁面，並將文字來回傳遞。

| 方向 | 使用技術 |
|------|----------|
| MainActivity → SecActivity | Intent with Bundle + `ActivityResultLauncher` |
| SecActivity → MainActivity | `setResult()` + `finish()` |

## 實作步驟

1. **STEP 01** — `MainActivity` 用 `registerForActivityResult(StartActivityForResult())` 宣告 `ActivityResultLauncher` 作為 Activity 啟動器。
2. **STEP 02** — `MainActivity` 把輸入文字放進 `Bundle`，夾帶在 `Intent` 中，用 `launch()` 前往 `SecActivity`。
3. **STEP 03** — `SecActivity` 讀出收到的文字並顯示；按下「回傳並返回」時，用 `setResult(RESULT_OK, intent)` 儲存要回傳的資料。
4. **STEP 04** — `SecActivity` 呼叫 `finish()` 結束自己，回到 `MainActivity`。
5. **STEP 05** — `MainActivity` 在 `ActivityResultLauncher` 的回呼中取得回傳資料，顯示在「從第二頁收到的回傳文字」。

## 檔案

- `app/src/main/java/com/example/activitypages/MainActivity.kt`
- `app/src/main/java/com/example/activitypages/SecActivity.kt`
- `app/src/main/res/layout/activity_main.xml`
- `app/src/main/res/layout/activity_sec.xml`
- `app/src/main/AndroidManifest.xml`

## 執行

用 Android Studio 開啟此資料夾，Gradle Sync 完成後執行 `app` 即可。
