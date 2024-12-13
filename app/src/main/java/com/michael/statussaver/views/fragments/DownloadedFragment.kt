package com.michael.statussaver.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.michael.statussaver.R
import com.michael.statussaver.databinding.FragmentDownloadedBinding


class DownloadedFragment : Fragment() {
    private var _binding: FragmentDownloadedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDownloadedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            noMediaHolder.visibility = View.VISIBLE
            noMediaWarning.noMediaTextView.text = getString(R.string.no_media_available_yet)
            noMediaWarning.openAppInfo.text = getString(R.string.open_whatsapp_or_business_whatsapp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}