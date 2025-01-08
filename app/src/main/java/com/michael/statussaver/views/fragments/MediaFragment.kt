package com.michael.statussaver.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.michael.statussaver.R
import com.michael.statussaver.data.StatusRepository
import com.michael.statussaver.databinding.FragmentMediaBinding
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.viewmodels.StatusViewModel
import com.michael.statussaver.viewmodels.factories.StatusViewModelFactory
import com.michael.statussaver.views.adapters.MediaAdapter
import kotlin.collections.reversed


class MediaFragment : Fragment() {
    private val binding by lazy { FragmentMediaBinding.inflate(layoutInflater) }
    lateinit var viewModel: StatusViewModel
    lateinit var adapter: MediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            arguments?.let {
                val repository = StatusRepository(requireActivity())
                viewModel = ViewModelProvider(
                    requireActivity(),
                    StatusViewModelFactory(repository)
                )[StatusViewModel::class.java]
                val mediaType = it.getString(Constants.MEDIA_TYPE_KEY, "")
                when (mediaType) {
                    Constants.MEDIA_TYPE_WHATSAPP_IMAGES -> {
                        viewModel.whatsAppImagesLiveData.observe(requireActivity()) { unFiltered ->
                            val filteredList = unFiltered.distinctBy { model ->
                                model.fileName
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.isEmpty()) {
                                noMediaAvailable.noMediaTextView.text = getString(R.string.no_image_available_now)
                                noMediaHolder.visibility = View.VISIBLE
                            } else {
//                                statusHolder.visibility = View.VISIBLE
                                noMediaHolder.visibility = View.GONE
                            }
                        }
                    }
                    Constants.MEDIA_TYPE_WHATSAPP_VIDEOS -> {
                        viewModel.whatsAppVideosLiveData.observe(requireActivity()) { unFiltered ->
                            val filteredList = unFiltered.distinctBy { model ->
                                model.fileName
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                noMediaAvailable.noMediaTextView.text = getString(R.string.no_video_available_now)
                                noMediaHolder.visibility = View.VISIBLE
                            } else {
                                noMediaHolder.visibility = View.GONE
                            }
                        }
                    }


                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES -> {
                        viewModel.whatsAppBusinessImagesLiveData.observe(requireActivity()) { unFiltered ->
                            val filteredList = unFiltered.distinctBy { model ->
                                model.fileName
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                noMediaAvailable.noMediaTextView.text = getString(R.string.no_image_available_now)
                                noMediaAvailable.openAppInfo.text = getString(R.string.open_whatsapp_business_info)
                                noMediaHolder.visibility = View.VISIBLE
                            } else {
                                noMediaHolder.visibility = View.GONE
                            }
                        }
                    }
                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS -> {
                        viewModel.whatsAppBusinessVideosLiveData.observe(requireActivity()) { unFiltered ->
                            val filteredList = unFiltered.distinctBy { model ->
                                model.fileName
                            }
                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                noMediaAvailable.noMediaTextView.text = getString(R.string.no_video_available_now)
                                noMediaAvailable.noMediaTextView.text = getString(R.string.open_whatsapp_business_info)
                                noMediaHolder.visibility = View.VISIBLE
                            } else {
                                noMediaHolder.visibility = View.GONE
                            }
                        }
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