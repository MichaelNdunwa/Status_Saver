package com.michael.statussaver.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michael.statussaver.databinding.ActivityVideosPreviewBinding

class VideosPreviewActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy { ActivityVideosPreviewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {

        }
    }
}