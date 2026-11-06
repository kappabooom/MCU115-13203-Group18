# 作業六 ThreadHandler + 背景音樂

以作業五的程式碼為基礎，加上：執行背景工作時播放背景音樂 (`background_music.mp3`)，完成或中斷時結束音樂。

- 音樂檔：`app/src/main/res/raw/background_music.mp3`
- `WorkViewModel` 改為繼承 `AndroidViewModel`（需要 `Application` 來建立 `MediaPlayer`）
- 按 **Start**：開始準備並執行背景工作，同時播放音樂（循環播放）
- 背景工作結束：顯示「背景工作結束！」並停止音樂
- 按 **Cancel**：立即中斷背景工作，顯示 Canceled 並立即停止音樂
- `MediaPlayer` 只在 main thread 操作；背景工作每 100ms 檢查一次是否被取消
- 每次 Start 都有自己的 job 編號，Cancel 後立刻再按 Start 也不會被舊的工作影響
- ViewModel 被清除 (`onCleared`) 時釋放 `MediaPlayer` 並結束 `HandlerThread`
