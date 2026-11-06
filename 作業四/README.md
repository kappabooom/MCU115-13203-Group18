# FoodOrderingWizard（第四週作業：Fast Food Ordering Wizard - Fragment）

Kotlin 點餐 App：一個主畫面 (Activity) + 四個副畫面 (Fragment)。

- **MainActivity**：主畫面，按鈕切換到各副畫面，顯示目前選擇與已提交的訂單
- **MainMealFragment**：主餐（單選）Burger / Fried Chicken / Chicken Nuggets
- **SideDishesFragment**：副餐（複選，至少一項）Fries / Salad / Corn Cup
- **DrinkFragment**：飲料（單選）Cola / Iced Tea / Water
- **ConfirmFragment**：確認頁面，按下 Confirm
  - 主餐、副餐、飲料都有選 → 跳出 AlertDialog 顯示所選項目和 Submit 按鈕，提交後回到主畫面並顯示所選餐點
  - 任何一項沒選 → 跳出 Toast：「Please select a main meal, at least one side dish, and a drink.」
- **OrderViewModel**：Activity 與 Fragment 共用（`activityViewModels()`）的訂單狀態
- **OrderModels.kt**：`Order` 資料類別（`isComplete()`、`summary()`）
- **WizardPagerAdapter**：ViewPager2 的 `FragmentStateAdapter`，管理四個 Fragment

用 Android Studio 開啟此資料夾即可執行。
