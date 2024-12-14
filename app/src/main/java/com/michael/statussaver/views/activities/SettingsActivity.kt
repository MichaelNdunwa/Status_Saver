package com.michael.statussaver.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.michael.statussaver.databinding.ActivitySettingsBinding
import com.michael.statussaver.utils.SharedPrefUtils
import com.michael.statussaver.utils.applyTheme

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolbar()
        setUpTheme()
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
    }

    private fun setUpTheme() {
        binding.apply {
            var index = 0
            var selectedTheme = SharedPrefUtils.getPrefString("theme", "Light")!!
            val themeChoice = arrayOf("System default", "Light", "Dark")
            displaySelectedTheme.text = selectedTheme // Set the initial theme text

            when (selectedTheme) {
                "System default" -> index = 0
                "Light" -> index = 1
                "Dark" -> index = 2
            }

            themeLayout.setOnClickListener {
                MaterialAlertDialogBuilder(this@SettingsActivity)
                    .setTitle("Choose theme")
                    .setSingleChoiceItems(themeChoice, index) { dialog, which ->
                        index = which
                        selectedTheme = themeChoice[which]
                    }
                    .setPositiveButton("Ok") { dialog, which ->
                        displaySelectedTheme.text = selectedTheme // Update the theme text
                        SharedPrefUtils.putPrefString("theme", selectedTheme)
                        when (selectedTheme) {
                            "System default" -> AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                            )

                            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                        recreate()
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun toolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}