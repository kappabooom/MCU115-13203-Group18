package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private val vm: OrderViewModel by viewModels()

    private lateinit var homeLayout: View
    private lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeLayout = findViewById(R.id.homeLayout)
        pager = findViewById(R.id.wizardPager)
        pager.adapter = WizardPagerAdapter(this)
        pager.isUserInputEnabled = false   // 只用按鈕切換頁面

        findViewById<Button>(R.id.btnChooseMain).setOnClickListener {
            showPage(WizardPagerAdapter.PAGE_MAIN)
        }
        findViewById<Button>(R.id.btnChooseSides).setOnClickListener {
            showPage(WizardPagerAdapter.PAGE_SIDES)
        }
        findViewById<Button>(R.id.btnChooseDrink).setOnClickListener {
            showPage(WizardPagerAdapter.PAGE_DRINK)
        }
        findViewById<Button>(R.id.btnOrder).setOnClickListener {
            showPage(WizardPagerAdapter.PAGE_CONFIRM)
        }

        val tvSelection = findViewById<TextView>(R.id.tvCurrentSelection)
        vm.order.observe(this) { order ->
            tvSelection.text = "Current Selection:\n${order.summary()}"
        }

        val tvSubmitted = findViewById<TextView>(R.id.tvSubmittedOrder)
        vm.submittedOrder.observe(this) { order ->
            if (order == null) {
                tvSubmitted.visibility = View.GONE
            } else {
                tvSubmitted.visibility = View.VISIBLE
                tvSubmitted.text = "Submitted Order:\n${order.summary()}"
            }
        }

        // 在副畫面時按返回鍵 -> 回主畫面
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (pager.visibility == View.VISIBLE) {
                    showHome()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        if (savedInstanceState?.getBoolean(KEY_PAGER_VISIBLE) == true) {
            homeLayout.visibility = View.GONE
            pager.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_PAGER_VISIBLE, pager.visibility == View.VISIBLE)
    }

    private fun showPage(page: Int) {
        pager.setCurrentItem(page, false)
        homeLayout.visibility = View.GONE
        pager.visibility = View.VISIBLE
    }

    /** 由 Fragment 呼叫 (Done / Submit) 回到主畫面 */
    fun showHome() {
        pager.visibility = View.GONE
        homeLayout.visibility = View.VISIBLE
    }

    companion object {
        private const val KEY_PAGER_VISIBLE = "pager_visible"
    }
}
