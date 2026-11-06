package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishesFragment : Fragment(R.layout.fragment_side_dishes) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boxes = mapOf(
            view.findViewById<CheckBox>(R.id.cbFries) to "Fries",
            view.findViewById<CheckBox>(R.id.cbSalad) to "Salad",
            view.findViewById<CheckBox>(R.id.cbCornCup) to "Corn Cup"
        )

        vm.order.observe(viewLifecycleOwner) { order ->
            boxes.forEach { (box, name) ->
                val shouldCheck = name in order.sideDishes
                if (box.isChecked != shouldCheck) box.isChecked = shouldCheck
            }
        }

        boxes.forEach { (box, name) ->
            box.setOnCheckedChangeListener { _, isChecked -> vm.setSideDish(name, isChecked) }
        }

        view.findViewById<Button>(R.id.btnDone).setOnClickListener {
            (activity as? MainActivity)?.showHome()
        }
    }
}
