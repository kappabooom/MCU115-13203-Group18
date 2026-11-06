package com.example.foodorderingwizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** Activity 與所有 Fragment 共用的 ViewModel，保存目前選擇與已提交的訂單 */
class OrderViewModel : ViewModel() {

    private val _order = MutableLiveData(Order())
    val order: LiveData<Order> = _order

    /** 提交後的訂單 (顯示在主畫面) */
    private val _submittedOrder = MutableLiveData<Order?>(null)
    val submittedOrder: LiveData<Order?> = _submittedOrder

    private fun current() = _order.value ?: Order()

    fun setMainMeal(meal: String) {
        _order.value = current().copy(mainMeal = meal)
    }

    fun setSideDish(side: String, checked: Boolean) {
        val sides = current().sideDishes.toMutableList()
        if (checked && side !in sides) sides.add(side)
        if (!checked) sides.remove(side)
        // 依菜單順序排列
        _order.value = current().copy(sideDishes = Menu.sideDishes.filter { it in sides })
    }

    fun setDrink(drink: String) {
        _order.value = current().copy(drink = drink)
    }

    fun submit() {
        _submittedOrder.value = current()
        _order.value = Order()
    }
}
