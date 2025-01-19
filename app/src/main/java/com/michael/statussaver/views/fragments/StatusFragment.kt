package com.michael.statussaver.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.michael.statussaver.databinding.FragmentStatusBinding
import com.michael.statussaver.R
import com.michael.statussaver.data.StatusRepository
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.SharedPrefKeys
import com.michael.statussaver.utils.SharedPrefUtils
import com.michael.statussaver.utils.getAllDownloadedFolderPermissions
import com.michael.statussaver.utils.getFolderPermissions
import com.michael.statussaver.viewmodels.StatusViewModel
import com.michael.statussaver.viewmodels.factories.StatusViewModelFactory
import com.michael.statussaver.views.activities.MainActivity
import com.michael.statussaver.views.adapters.MediaViewPagerAdapter


class StatusFragment : Fragment() {

    private val binding by lazy { FragmentStatusBinding.inflate(layoutInflater) }
    private lateinit var type: String
    private val WHATSAPP_REQUEST_CODE = 100
    private val WHATSAPP_BUSINESS_REQUEST_CODE = 101
    private val DOWNLOADED_REQUEST_CODE = 102
//    private val activity = requireActivity()
    private val viewPagerTitles = arrayListOf("Images", "Videos", "Audio")
    lateinit var viewModel: StatusViewModel
    val TAG = "StatusFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            arguments?.let {
                type = it.getString(Constants.FRAGMENT_TYPE_KEY, "")
                val repository = StatusRepository(requireActivity())
                viewModel = ViewModelProvider(
                    requireActivity(), StatusViewModelFactory(repository)
                )[StatusViewModel::class.java]

                type = it.getString(Constants.FRAGMENT_TYPE_KEY, "")

                when (type) {
                    Constants.STATUS_TYPE_WHATSAPP -> {
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED, false
                        )
                        if (isPermissionGranted) {
                            getWhatsAppStatus()
                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatus()
                            }
                        }
                        givePermissionAccess.allowPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUET_CODE = WHATSAPP_REQUEST_CODE,
                                initialUri = Constants.getWhatsAppStatusUriPath()
                            )
                        }
                        val viewPagerAdapter = MediaViewPagerAdapter(
                            fragmentActivity = requireActivity(),
                            imagesType = Constants.MEDIA_TYPE_WHATSAPP_IMAGES,
                            videosType = Constants.MEDIA_TYPE_WHATSAPP_VIDEOS,
                            audiosType = Constants.MEDIA_TYPE_WHATSAPP_AUDIOS
                        )
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager) { tab, pos ->
                            tab.text = viewPagerTitles[pos]
                        }.attach()
                    }

                    Constants.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED, false
                        )
                        if (isPermissionGranted) {
                            getWhatsAppBusinessStatus()
                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatus()
                            }
                        }
                        givePermissionAccess.allowPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUET_CODE = WHATSAPP_BUSINESS_REQUEST_CODE,
                                initialUri = Constants.getWhatsAppBusinessStatusUriPath()
                            )
                        }
                        val viewPagerAdapter = MediaViewPagerAdapter(
                            fragmentActivity = requireActivity(),
                            imagesType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES,
                            videosType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS,
                            audiosType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_AUDIOS
                        )
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager) { tab, pos ->
                            tab.text = viewPagerTitles[pos]
                        }.attach()
                    }


                    Constants.STATUS_TYPE_DOWNLOADED -> {
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_DOWNLOADED_PERMISSION_GRANTED, false
                        )
                        if (isPermissionGranted) {
                            getDownloadedStatus()
                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatus()
                            }
                        }
                        givePermissionAccess.allowPermission.setOnClickListener {
//                            getFolderPermissions(
//                                context = requireActivity(),
//                                REQUET_CODE = DOWNLOADED_REQUEST_CODE,
//                                initialUri = Constants.getDownloadedStatusUriPath()
//                            )
                            getAllDownloadedFolderPermissions(context = requireActivity(), REQUET_CODE = DOWNLOADED_REQUEST_CODE)
                        }
                        val viewPagerAdapter = MediaViewPagerAdapter(
                            fragmentActivity = requireActivity(),
                            imagesType = Constants.MEDIA_TYPE_DOWNLOADED_IMAGES,
                            videosType = Constants.MEDIA_TYPE_DOWNLOADED_VIDEOS,
                            audiosType = Constants.MEDIA_TYPE_DOWNLOADED_AUDIOS
                        )
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager) { tab, pos ->
                            tab.text = viewPagerTitles[pos]
                        }.attach()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val treeUri = data?.data!!
            requireActivity().contentResolver.takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            if (requestCode == WHATSAPP_REQUEST_CODE) {
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED, true)
                getWhatsAppStatus()
                Log.d(TAG, "Access granted to: $treeUri")
            } else if (requestCode == WHATSAPP_BUSINESS_REQUEST_CODE) {
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED, true)
                getWhatsAppBusinessStatus()
                Log.d(TAG, "Access granted to: $treeUri")
            } else if (requestCode == DOWNLOADED_REQUEST_CODE)  {
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_DOWNLOADED_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_DOWNLOADED_PERMISSION_GRANTED, true)
                getDownloadedStatus()
                Log.d(TAG, "Access granted to: $treeUri")
            }
        }
    }

    fun getWhatsAppStatus() {
        binding.permissionHolder.visibility = View.GONE
        binding.tabLayout.visibility = View.VISIBLE
        viewModel.getWhatsAppStatus()
    }
    fun getWhatsAppBusinessStatus() {
        binding.permissionHolder.visibility = View.GONE
        binding.tabLayout.visibility = View.VISIBLE
        viewModel.getWhatsAppBusinessStatus()
    }
    fun getDownloadedStatus() {
        binding.permissionHolder.visibility = View.GONE
        binding.tabLayout.visibility = View.VISIBLE
        viewModel.getDownloadedStatus()
    }

    fun refreshStatus() {
        when (type) {
            Constants.STATUS_TYPE_WHATSAPP -> {
                Toast.makeText(activity, "Refreshing WhatsApp Status", Toast.LENGTH_SHORT).show()
                    getWhatsAppStatus()
            }
            Constants.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                Toast.makeText(activity, "Refreshing WhatsApp Business Status", Toast.LENGTH_SHORT).show()
                getWhatsAppBusinessStatus()
            }
        }
        Handler(Looper.myLooper()!!).postDelayed({
            binding.swipeRefreshLayout.isRefreshing = false
        }, 2000)
//        (requireActivity() as MainActivity).recreate()
    }

}