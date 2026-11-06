package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class MainMealFragment : Fragment(R.layout.fragment_main_meal) {

    private val vm: OrderViewModel by activityViewModels()

    private val idToMeal = mapOf(
        R.id.rbBurger to "Burger",
        R.id.rbFriedChicken to "Fried Chicken",
        R.id.rbNuggets to "Chicken Nuggets"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val group = view.findViewById<RadioGroup>(R.id.rgMainMeal)

        // 與 ViewModel 同步 (例如提交後被清空)
        vm.order.observe(viewLifecycleOwner) { order ->
            val id = idToMeal.entries.firstOrNull { it.value == order.mainMeal }?.key
            if (id == null) {
                if (group.checkedRadioButtonId != View.NO_ID) group.clearCheck()
            } else if (group.checkedRadioButtonId != id) {
                group.check(id)
            }
        }

        group.setOnCheckedChangeListener { _, checkedId ->
            idToMeal[checkedId]?.let { vm.setMainMeal(it) }
        }

        view.findViewById<Button>(R.id.btnDone).setOnClickListener {
            (activity as? MainActivity)?.showHome()
        }
    }
}
