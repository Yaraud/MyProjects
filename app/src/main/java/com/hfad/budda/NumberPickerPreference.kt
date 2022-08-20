package com.hfad.budda

import android.content.Context
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import com.hfad.budda.ui.viewmodels.QuoteViewModel


class NumberPickerPreference(context: Context,
                             private val viewModel: QuoteViewModel
): Preference(context) {

    private lateinit var numberPicker: NumberPicker

    override fun onClick() {
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        numberPicker = NumberPicker(context)
        numberPicker.minValue = 1
        numberPicker.maxValue = 10
        builder?.setView(numberPicker)
        builder?.setTitle("Choose number of repeats")
        builder?.setPositiveButton("OK") { _, _ ->
            val interval = numberPicker.value
            viewModel.updateInterval(interval)
            summary = if (interval.toString() == "1")
                "$interval time"
            else
                "$interval times"
        }
        builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder?.show()
    }
}