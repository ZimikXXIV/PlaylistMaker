package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<SettingViewModel>()
    private fun setEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            viewModel.shareLink()
        }

        binding.btnSupport.setOnClickListener {
            viewModel.sendMail()
        }

        binding.btnLicense.setOnClickListener {
            viewModel.openTerms()
        }

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.loadTheme()

        viewModel.getSettingsState().observe(this) { settingsState ->
            binding.switchDayNight.isChecked = settingsState.isDarkThemeEnabled
        }

        setEvents()
    }
}
