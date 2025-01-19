package com.michael.statussaver.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.michael.statussaver.databinding.ItemImagePreviewBinding
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.R
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.SharedPrefUtils
import com.michael.statussaver.utils.isStatusExist
import com.michael.statussaver.utils.saveStatus

class ImagesPreviewAdapter(val list: ArrayList<MediaModel>, val context: Context) :
    RecyclerView.Adapter<ImagesPreviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemImagePreviewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model = list[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: ItemImagePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(mediaModel: MediaModel) {
                binding.apply {
                    Glide.with(context).load(mediaModel.pathUri.toUri()).into(zoomableImageView)

//                    val downloadImage = if (mediaModel.isDownloaded) {
//                        R.drawable.icon_check
//                    } else {
//                        R.drawable.icon_download_2
//                    }
//                    val downloadText = if (mediaModel.isDownloaded) {
//                        "Saved"
//                    } else {
//                        "Download"
//                    }
//                    mediaModel.isDownloaded = SharedPrefUtils.getPrefDownloadState(mediaModel.fileName)
//                    val downloadImage = if (mediaModel.isDownloaded) R.drawable.icon_check else R.drawable.icon_download_2
//                    val downloadText = if (mediaModel.isDownloaded) "Saved" else "Download"
                    val downloadImage = if (context.isStatusExist(mediaModel.fileName, mediaModel.fileType)) R.drawable.icon_check else R.drawable.icon_download_2
                    val downloadText = if (context.isStatusExist(mediaModel.fileName, mediaModel.fileType)) "Saved" else "Download"
                    tools.downloadIcon.setImageResource(downloadImage)
                    tools.downloadTextview.text = downloadText

                    tools.downloadButton.setOnClickListener {
                        val isDownloaded = context.saveStatus(mediaModel)
                        if (isDownloaded) {
//                            SharedPrefUtils.putPrefDownloadState(mediaModel.fileName, true)
                            mediaModel.isDownloaded = true
                            tools.downloadIcon.setImageResource(R.drawable.icon_check)
                            tools.downloadTextview.text = "Saved"
                        } else {
                            Toast.makeText(context, "Unable to save", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Share button:
                    tools.shareButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, mediaModel.pathUri.toUri())
                        intent.putExtra(Intent.EXTRA_TEXT, "See this awesome status I saw on Status downloader app.")
                        context.startActivity(Intent.createChooser(intent, "Share Status"))
                    }

                    // Repost button:
                    tools.repostButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.setPackage("com.whatsapp")
                        intent.putExtra(Intent.EXTRA_STREAM, mediaModel.pathUri.toUri())
                        context.startActivity(intent)
                    }
//                    Intent().putExtra(Constants.MEDIA_LIST_KEY, list)
                }
            }
    }
}