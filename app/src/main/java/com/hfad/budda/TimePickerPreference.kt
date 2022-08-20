package com.hfad.budda

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.hfad.budda.ui.viewmodels.QuoteViewModel

class TimePickerPreference(context: Context,
                           private val viewModel: QuoteViewModel,
                           private val fragmentManager: FragmentManager,
                           private val tag: String,
                           private val typeOfTime: String
): Preference(context) {

    private lateinit var timePicker: MaterialTimePicker

    override fun onClick() {
        timePicker = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()
        timePicker.show(fragmentManager,tag)
        timePicker.addOnPositiveButtonClickListener{
            summary = String.format("%d:%02d", timePicker.hour, timePicker.minute)
            when (typeOfTime) {
                "first" -> viewModel.updateFirstTime(
                   String.format("%d:%d", timePicker.hour, timePicker.minute)
               )
                "second" -> {
                    viewModel.updateSecondTime(
                        String.format("%d:%d", timePicker.hour, timePicker.minute)
                    )
                }

            }
        }
    }
}