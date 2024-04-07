package com.example.playlistmaker.Settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Settings.presentation.viewmodel.SettingViewModel
import com.example.playlistmaker.Settings.presentation.viewmodel.SettingViewModel.Companion.getViewModelFactory
import com.example.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: SettingViewModel

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

        viewModel = ViewModelProvider(this, getViewModelFactory(this))[SettingViewModel::class.java]
        viewModel.loadTheme()


        viewModel.getSettingsState().observe(this) { settingsState ->
            binding.switchDayNight.isChecked = settingsState.isDarkThemeEnabled
        }

        setEvents()
    }
}
