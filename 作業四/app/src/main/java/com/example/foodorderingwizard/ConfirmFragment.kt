package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ConfirmFragment : Fragment(R.layout.fragment_confirm) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvSummary = view.findViewById<TextView>(R.id.tvOrderSummary)

        vm.order.observe(viewLifecycleOwner) { order ->
            tvSummary.text = order.summary()
        }

        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            onConfirmClicked()
        }
    }

    private fun onConfirmClicked() {
        val order = vm.order.value

        // 有任何一項沒選 -> Toast 提示
        if (order?.isComplete() != true) {
            Toast.makeText(
                requireContext(),
                "Please select a main meal, at least one side dish, and a drink.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // 全部都有選 -> AlertDialog 顯示所選項目與 Submit 按鈕
        AlertDialog.Builder(requireContext())
            .setTitle("Submit Order")
            .setMessage(
                "Main: ${order.mainMeal}\n" +
                "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                "Drink: ${order.drink}\n\nSubmit this order?"
            )
            .setPositiveButton("Submit") { dialog, _ ->
                dialog.dismiss()
                vm.submit()
                Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()
                // 提交後跳回主畫面並顯示所選餐點
                (activity as? MainActivity)?.showHome()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
