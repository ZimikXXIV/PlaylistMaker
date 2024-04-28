package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.presentation.viewmodel.SettingViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentSettings : BindingFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModel<SettingViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadTheme()

        viewModel.getSettingsState().observe(viewLifecycleOwner) { settingsState ->
            binding.switchDayNight.isChecked = settingsState.isDarkThemeEnabled
        }

        setEvents()

    }
    private fun setEvents() {

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


}

