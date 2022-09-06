package com.hfad.buddha

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.hfad.buddha.viewmodels.QuoteViewModel
import com.hfad.buddha.viewmodels.QuoteViewModelFactory

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: QuoteViewModel by viewModels {
        QuoteViewModelFactory(
            (activity?.application as BuddhaApplication).database.quotesDao(),
            (activity?.application as BuddhaApplication).database.settingsDao()
        )
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.layout_settings, rootKey)

        val preferenceCategory = findPreference<PreferenceCategory>("category")

        /**
         * Start Time
         */
        val startTimePicker = activity?.supportFragmentManager?.let {
            TimePickerPreference(preferenceCategory!!.context,viewModel,
                it,"SettingsFragment","first")
        }
        preferenceCategory?.addPreference(startTimePicker)
        startTimePicker?.title = "Choose start of notifications"
        startTimePicker?.key = "first_time"
        viewModel.getFirstTime().let {
            val time = it.split(":")
            startTimePicker?.summary = String.format("%d:%02d", time[0].toInt(), time[1].toInt())
        }

        /**
         * End Time
         */
        val endTimePicker = activity?.supportFragmentManager?.let {
            TimePickerPreference(preferenceCategory!!.context,viewModel,
                it,"SettingsFragment","second")
        }
        preferenceCategory?.addPreference(endTimePicker)
        endTimePicker?.title = "Choose end of notifications"
        endTimePicker?.key = "second_time"
        viewModel.getSecondTime().let {
            val time = it.split(":")
            endTimePicker?.summary = String.format("%d:%02d", time[0].toInt(), time[1].toInt())
        }

        /**
         * Repeat Time
         */
        val numberPicker = NumberPickerPreference(preferenceCategory!!.context,viewModel)
        preferenceCategory.addPreference(numberPicker)
        numberPicker.title = "Choose number of repeats"
        numberPicker.key = "interval"
        viewModel.getInterval()?.let {
            if (it.toString() == "1")
                numberPicker.summary = "$it time"
            else
                numberPicker.summary = "$it times" }
        /**
         * Enable notifications
         */

        val isNotifyEnabled = findPreference<CheckBoxPreference>("notify_check")
        isNotifyEnabled?.setOnPreferenceChangeListener { _, _ ->
            if(isNotifyEnabled.isChecked) {
                isNotifyEnabled.isChecked = false
                viewModel.updateNotify(false)
            }
            else {
                isNotifyEnabled.isChecked = true
                viewModel.updateNotify(true)
            }

            return@setOnPreferenceChangeListener true
        }


        val switchTheme = findPreference<SwitchPreference>("theme_switch")
        switchTheme?.setOnPreferenceChangeListener { _, _ ->
            if (!switchTheme.isChecked) {
                viewModel.updateTheme(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                viewModel.updateTheme(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return@setOnPreferenceChangeListener true
        }
    }
}