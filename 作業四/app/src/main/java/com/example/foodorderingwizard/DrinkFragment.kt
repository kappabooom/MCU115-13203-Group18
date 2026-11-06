package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DrinkFragment : Fragment(R.layout.fragment_drink) {

    private val vm: OrderViewModel by activityViewModels()

    private val idToDrink = mapOf(
        R.id.rbCola to "Cola",
        R.id.rbIcedTea to "Iced Tea",
        R.id.rbWater to "Water"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val group = view.findViewById<RadioGroup>(R.id.rgDrink)

        vm.order.observe(viewLifecycleOwner) { order ->
            val id = idToDrink.entries.firstOrNull { it.value == order.drink }?.key
            if (id == null) {
                if (group.checkedRadioButtonId != View.NO_ID) group.clearCheck()
            } else if (group.checkedRadioButtonId != id) {
                group.check(id)
            }
        }

        group.setOnCheckedChangeListener { _, checkedId ->
            idToDrink[checkedId]?.let { vm.setDrink(it) }
        }

        view.findViewById<Button>(R.id.btnDone).setOnClickListener {
            (activity as? MainActivity)?.showHome()
        }
    }
}
