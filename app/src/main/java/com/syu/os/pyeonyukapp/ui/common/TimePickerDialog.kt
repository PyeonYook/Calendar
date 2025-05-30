package com.syu.os.pyeonyukapp.ui.common

import android.app.TimePickerDialog
import android.content.Context
import java.time.LocalTime

fun showTimePicker(
    context: Context,
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val hour = initialTime.hour
    val minute = initialTime.minute

    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
        },
        hour,
        minute,
        false
    ).show()
}
