package com.michael.statussaver.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.statussaver.databinding.FragmentStatusBinding
import com.michael.statussaver.utils.Constant
import com.michael.statussaver.R

class StatusFragment : Fragment() {
    private val activity = this
    private val binding by lazy { FragmentStatusBinding.inflate(layoutInflater) }
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            statusViewPager.visibility = View.GONE
            arguments?.let {
                type = it.getString(Constant.FRAGMENT_TYPE_KEY, "")
                // set open app info:
                when (type) {
                    Constant.STATUS_TYPE_WHATSAPP -> {
                        noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_info)
                    }
                    Constant.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                        noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_business_info)
                    }
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}