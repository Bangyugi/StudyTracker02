package com.bangvan.studytracker.utils

import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDeadlineFormat(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.US)
    return format.format(date)
}

fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        this.then(modifier())
    } else {
        this
    }
}
