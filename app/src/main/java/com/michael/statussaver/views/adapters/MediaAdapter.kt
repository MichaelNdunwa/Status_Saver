package com.michael.statussaver.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.michael.statussaver.databinding.ItemMediaBinding
import com.michael.statussaver.models.MEDIA_TYPE_IMAGE
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.saveStatus
import com.michael.statussaver.views.activities.ImagesPreviewActivity
import com.michael.statussaver.views.activities.VideosPreviewActivity

class MediaAdapter(val list: ArrayList<MediaModel>, val context: Context) :
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(mediaModel: MediaModel) {
                binding.apply {
                    // Load image and video into image view
                    Glide.with(context)
                        .load(mediaModel.pathUri.toUri())
                        .into(statusImage)
                    // hide play button for images
                    if (mediaModel.fileType == MEDIA_TYPE_IMAGE) {
                        playButton.visibility = View.GONE
                    }
                    // show download check if media is downloaded:
                    if (mediaModel.isDownloaded) {
                        downloadText.visibility = View.GONE
                        downloadCheck.visibility = View.VISIBLE
                    /*   downloadText.visibility = View.VISIBLE
                        downloadCheck.visibility = View.GONE*/
                    } else {
                        downloadText.visibility = View.VISIBLE
                        downloadCheck.visibility = View.GONE
                      /*  downloadText.visibility = View.GONE
                        downloadCheck.visibility = View.VISIBLE*/
                    }
                    // set click listener for status card:
                    statusCard.setOnClickListener {
                        if (mediaModel.fileType == MEDIA_TYPE_IMAGE) {
                            // go to images preview activity
                            Intent().apply {
                                putExtra(Constants.MEDIA_LIST_KEY, list)
                                putExtra(Constants.MEDIA_SCROLL_KEY, layoutPosition)
                                setClass(context, ImagesPreviewActivity::class.java)
                                context.startActivity(this)
                            }
                        } else {
                            // go to video preview activity
                            Intent().apply {
                                putExtra(Constants.MEDIA_LIST_KEY, list)
                                putExtra(Constants.MEDIA_SCROLL_KEY, layoutPosition)
                                setClass(context, VideosPreviewActivity::class.java)
                                context.startActivity(this)
                            }
                        }
                    }

                    downloadHolder.setOnClickListener {
                        val isDownload = context.saveStatus(mediaModel)
                        if (isDownload) {
                            // Status saved successfully
//                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            mediaModel.isDownloaded = true
                            downloadCheck.visibility = View.VISIBLE
                            downloadText.visibility = View.GONE
                        } else {
                            // unable to download status:
                            Toast.makeText(context, "Unable to Save", Toast.LENGTH_SHORT).show()
                        }
                    }
                    /*var isToastShown = false
                    val isDownload = context.saveStatus(mediaModel)
                    if (isDownload) {
                        // Status saved successfully
                        if (!isToastShown) {
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            isToastShown = true
                        }
                        mediaModel.isDownloaded = true
                        downloadCheck.visibility = View.VISIBLE
                        downloadText.visibility = View.GONE
                    } else {
                        // unable to download status:
                        if (!isToastShown) {
                            Toast.makeText(context, "Unable to Save", Toast.LENGTH_SHORT).show()
                            isToastShown = true
                        }
                    }*/
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMediaBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = list.size

}