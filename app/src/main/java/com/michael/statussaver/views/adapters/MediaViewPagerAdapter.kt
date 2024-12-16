package com.michael.statussaver.views.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.views.fragments.MediaFragment

class MediaViewPagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val imagesType: String,
    private val videosType: String
) : FragmentStateAdapter(fragmentActivity) {
    override  fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                // Image media fragment
                val mediaFragment = MediaFragment()
                val bundle = Bundle()
                bundle.putString(Constants.MEDIA_TYPE_KEY, imagesType)
                mediaFragment.arguments = bundle
                mediaFragment
            }
            else -> {
                // Videos media fragment:
                val mediaFragment = MediaFragment()
                val bundle = Bundle()
                bundle.putString(Constants.MEDIA_TYPE_KEY, videosType)
                mediaFragment.arguments = bundle
                mediaFragment
            }
        }
    }
}
