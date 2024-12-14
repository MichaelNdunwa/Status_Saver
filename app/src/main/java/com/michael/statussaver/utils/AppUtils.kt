package com.michael.statussaver.utils

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.michael.statussaver.R


fun Activity.replaceFragment(fragment: Fragment, arg: Bundle? = null) {
    val fragmentActivity: FragmentActivity = this as FragmentActivity
    fragmentActivity.supportFragmentManager.beginTransaction().apply {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        arg?.let { fragment.arguments = it }
        replace(R.id.frame_layout, fragment)
        addToBackStack(null)
    }.commit()
}

fun Activity.applyTheme() {
    val theme = SharedPrefUtils.getPrefString("theme", "Light")
      when (theme) {
            "System default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
}