package com.michael.statussaver.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.michael.statussaver.R
import com.michael.statussaver.databinding.FragmentStatusBinding
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.SharedPrefKeys
import com.michael.statussaver.utils.SharedPrefUtils
import com.michael.statussaver.utils.getFolderPermissions
import com.michael.statussaver.viewmodels.StatusViewModel

class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private lateinit var type: String
    private val WHATSAPP_REQUEST_CODE = 101
    private val WHATSAPP_BUSINESS_REQUEST_CODE = 102
    private val viewPagerTitles = arrayListOf("Images", "Videos")
    lateinit var viewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatusBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            arguments?.let {
                type = it.getString(Constants.FRAGMENT_TYPE_KEY, "")
                // set open app info:
                when (type) {
                    Constants.STATUS_TYPE_WHATSAPP -> {
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,
                            false
                        )
                    /*    if (isPermissionGranted) {
                            getWhatsAppStatus()
                        }*/
                        givePermissionAccess.allowPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUET_CODE = WHATSAPP_REQUEST_CODE,
                                initialUri = Constants.getWhatsAppStatusUriPath()
                            )
                            noMediaHolder.visibility = View.VISIBLE
                            noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_info)
                        }


                    }

                    Constants.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                        givePermissionAccess.allowPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUET_CODE = WHATSAPP_BUSINESS_REQUEST_CODE,
                                initialUri = Constants.getWhatsAppBusinessStatusUriPath()
                            )
                            noMediaHolder.visibility = View.VISIBLE
                            noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_business_info)
                        }

                    }
                }
            }

        }


    }

    fun getWhatsAppStatus() {
        binding.permissionHolder.visibility = View.GONE
    }
    fun getWhatsAppBusinessStatus() {
        binding.permissionHolder.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        val fragment: Fragment? = (requireActivity() as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.frame_layout)

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
            } else if (resultCode == WHATSAPP_BUSINESS_REQUEST_CODE) {
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED, true)
                getWhatsAppBusinessStatus()
            }
        }
    }





     override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}