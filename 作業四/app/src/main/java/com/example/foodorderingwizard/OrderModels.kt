package com.example.foodorderingwizard

/** 一筆訂單：主餐(單選)、副餐(可複選，至少一項)、飲料(單選) */
data class Order(
    val mainMeal: String? = null,
    val sideDishes: List<String> = emptyList(),
    val drink: String? = null
) {
    fun isComplete(): Boolean =
        mainMeal != null && sideDishes.isNotEmpty() && drink != null

    fun summary(): String =
        "Main: ${mainMeal ?: "--"}\n" +
        "Sides: ${if (sideDishes.isEmpty()) "--" else sideDishes.joinToString(", ")}\n" +
        "Drink: ${drink ?: "--"}"
}

object Menu {
    val mainMeals = listOf("Burger", "Fried Chicken", "Chicken Nuggets")
    val sideDishes = listOf("Fries", "Salad", "Corn Cup")
    val drinks = listOf("Cola", "Iced Tea", "Water")
}
