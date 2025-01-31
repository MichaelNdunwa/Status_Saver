package com.michael.statussaver.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.michael.statussaver.databinding.ItemImagePreviewBinding
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.R
import com.michael.statussaver.databinding.ItemVideoPreviewBinding
import com.michael.statussaver.utils.saveStatus

class VideoPreviewAdapter(val list: ArrayList<MediaModel>, val context: Context) :
    RecyclerView.Adapter<VideoPreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemVideoPreviewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model = list[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: ItemVideoPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(mediaModel: MediaModel) {
                binding.apply {
                    // player
                    val player = ExoPlayer.Builder(context).build()
                    playerView.player = player
                    val mediaItem = MediaItem.fromUri(mediaModel.pathUri)
                    player.setMediaItem(mediaItem)
                    player.prepare()

                    // download button action:
                    val downloadImage = if (mediaModel.isDownloaded) {
                        R.drawable.icon_check
                    } else {
                        R.drawable.icon_download_2
                    }
                    val downloadText = if (mediaModel.isDownloaded) {
                        "Saved"
                    } else {
                        "Download"
                    }
                    tools.downloadIcon.setImageResource(downloadImage)
                    tools.downloadTextview.text = downloadText
                    tools.downloadButton.setOnClickListener {
                        val isDownloaded = context.saveStatus(mediaModel)
                        if (isDownloaded) {
                            // status is downloaded/saved
//                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            mediaModel.isDownloaded = true
                            tools.downloadIcon.setImageResource(R.drawable.icon_check)
                            tools.downloadTextview.text = "Saved"
                        } else {
                            Toast.makeText(context, "Unable to Save", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Share button:
                    tools.shareButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "video/*"
                        intent.putExtra(Intent.EXTRA_STREAM, mediaModel.pathUri.toUri())
                        intent.putExtra(Intent.EXTRA_TEXT, "Check out this video")
                        context.startActivity(Intent.createChooser(intent, "Share with"))
                    }

                    // Repost button:
                    tools.repostButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "video/*"
                        intent.setPackage("com.whatsapp")
                        intent.putExtra(Intent.EXTRA_STREAM, mediaModel.pathUri.toUri())
                        context.startActivity(intent)
                    }
                }
            }

        fun stopPlayer() {
            binding.playerView.player?.stop()
        }
    }
}