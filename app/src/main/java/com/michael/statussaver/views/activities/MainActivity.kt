package com.michael.statussaver.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.michael.statussaver.R
import com.michael.statussaver.databinding.ActivityMainBinding
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.SharedPrefUtils
import com.michael.statussaver.utils.applyTheme
import com.michael.statussaver.utils.replaceFragment
import com.michael.statussaver.views.fragments.StatusFragment

class MainActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefUtils.init(activity)
        setContentView(binding.root)
        bottomNavigation()
        // setOnClickListener for the settings icon:
        binding.settingsIcon.setOnClickListener { startActivity(Intent(activity, SettingsActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
    }

    private fun bottomNavigation() {
        binding.apply {
            val bundle = Bundle()
//            bottomNavigation.menu.getItem(0).isChecked = true
            bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.STATUS_TYPE_WHATSAPP)
//            bundle.putString(Constants.FRAGMENT_TYPE_KEY, "")
            replaceFragment(StatusFragment(), bundle)

            bottomNavigation.setOnItemSelectedListener { item ->
                // Handle Fragments:
                when (item.itemId) {
                    R.id.whatsapp -> {
                        // WhatsApp Fragment
//                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.STATUS_TYPE_WHATSAPP)
                        replaceFragment(StatusFragment(), bundle)
                    }

                    R.id.w_business -> {
                        // W.Business Fragment
//                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.STATUS_TYPE_WHATSAPP_BUSINESS)
                        replaceFragment(StatusFragment(), bundle)
                    }

                    R.id.downloaded -> {
                        // Downloaded Fragment
//                        replaceFragment(DownloadedFragment())
//                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.STATUS_TYPE_DOWNLOADED)
                        replaceFragment(StatusFragment(), bundle)
                    }
                }
                return@setOnItemSelectedListener true
            }
        }

    }

//    private fun setIndicatorPosition(selectedItemIndex: Int) {
//        binding.apply {
//            bottomNavigation.viewTreeObserver.addOnGlobalLayoutListener {
//                val menuItemWidth = bottomNavigation.width / bottomNavigation.menu.size()
//                val newXPosition =
//                    menuItemWidth * selectedItemIndex + menuItemWidth / 2 - indicator.width / 2
//
//                // Animate the indicator to the new position
//                indicator.animate()
//                    .translationX(newXPosition.toFloat())
//                    .setDuration(50)
//                    .start()
//                indicator.visibility = View.VISIBLE
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

}

