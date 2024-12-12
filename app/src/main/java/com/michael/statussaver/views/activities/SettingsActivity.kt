package com.michael.statussaver.views.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.michael.statussaver.R
import com.michael.statussaver.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(toolbar)
//            supportActionBar?.setDisplayShowHomeEnabled(true)
//            supportActionBar!!.setLogo(R.drawable.icon_back)
//            supportActionBar!!.setDisplayUseLogoEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}