package com.michael.statussaver.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michael.statussaver.databinding.ActivityImagePreviewBinding
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.views.adapters.ImagesPreviewAdapter
import java.util.ArrayList

class ImagesPreviewActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy { ActivityImagePreviewBinding.inflate(layoutInflater) }
    private lateinit var adapter: ImagesPreviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        toolbar()
        binding.apply {
            val list =
                intent.getSerializableExtra(Constants.MEDIA_LIST_KEY) as ArrayList<MediaModel>
            val scrollTo = intent.getIntExtra(Constants.MEDIA_SCROLL_KEY, 0)
            adapter = ImagesPreviewAdapter(list, activity)
            imageViewPager.adapter = adapter
            imageViewPager.currentItem = scrollTo
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun toolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }
}