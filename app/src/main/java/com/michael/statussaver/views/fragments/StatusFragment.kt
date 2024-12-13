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
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val activity = this
    private lateinit var type: String

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
                type = it.getString(Constant.FRAGMENT_TYPE_KEY, "")
                // set open app info:
                when (type) {
                    Constant.STATUS_TYPE_WHATSAPP -> {
                        permissionHolder.visibility = View.VISIBLE
                        noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_info)
                    }
                    Constant.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                        permissionHolder.visibility = View.VISIBLE
                        noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_business_info)
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}