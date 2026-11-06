# 作業五 ThreadHandler and Progress Bar

- 一個 Activity + ViewPager2(TabLayout:Start / Progress / Result)
- `WorkViewModel` 用 `HandlerThread` + `Handler` 執行背景工作,透過 `LiveData` 通知三個 Fragment
- 按 **Start**:先準備 10 秒(Preparing…,圓環轉圈),再進行 100 步工作並顯示百分比
- **Result** 頁顯示 Preparing… → Working… → 背景工作結束!
- 按 **Cancel**:`handlerThread.interrupt()` 立即中斷背景工作,顯示 Canceled
- 原本的直線 Progress Bar 改為圓形漸層 Progress Bar,% 顯示在中間(`res/drawable/circular_progress.xml`)
