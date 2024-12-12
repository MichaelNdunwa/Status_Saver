package com.michael.statussaver.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.michael.statussaver.R
import com.michael.statussaver.databinding.ActivityMainBinding
import com.michael.statussaver.utils.Constant
import com.michael.statussaver.utils.replaceFragment
import com.michael.statussaver.views.fragments.DownloadedFragment
import com.michael.statussaver.views.fragments.StatusFragment

class MainActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bottomNavigation()
        // setOnClickListener for the settings icon:
        binding.settingsIcon.setOnClickListener { startActivity(Intent(activity, SettingsActivity::class.java)) }
    }

    private fun bottomNavigation() {
        binding.apply {

            bottomNavigation.menu.getItem(0).isChecked = true
            setIndicatorPosition(0)

            bottomNavigation.setOnItemSelectedListener { item ->
                // Handle Fragments:
                val bundle = Bundle()
                when (item.itemId) {
                    R.id.whatsapp -> {
                        // WhatsApp Fragment
                        bundle.putString(Constant.FRAGMENT_TYPE_KEY, Constant.STATUS_TYPE_WHATSAPP)
                        replaceFragment(StatusFragment())
                        setIndicatorPosition(0)
                    }

                    R.id.w_business -> {
                        // W.Business Fragment
                        bundle.putString(Constant.FRAGMENT_TYPE_KEY, Constant.STATUS_TYPE_WHATSAPP_BUSINESS)
                        replaceFragment(StatusFragment(), bundle)
                        setIndicatorPosition(1)
                    }

                    R.id.downloaded -> {
                        // Downloaded Fragment
                        replaceFragment(DownloadedFragment())
                        setIndicatorPosition(2)
                    }
                }

                true
            }
        }

    }

    private fun setIndicatorPosition(selectedItemIndex: Int) {
        binding.apply {
            bottomNavigation.viewTreeObserver.addOnGlobalLayoutListener {
                val menuItemWidth = bottomNavigation.width / bottomNavigation.menu.size()
                val newXPosition =
                    menuItemWidth * selectedItemIndex + menuItemWidth / 2 - indicator.width / 2

                // Animate the indicator to the new position
                indicator.animate()
                    .translationX(newXPosition.toFloat())
                    .setDuration(200)
                    .start()
                indicator.visibility = View.VISIBLE
            }
        }
    }

}

