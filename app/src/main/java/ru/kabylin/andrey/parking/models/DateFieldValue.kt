package ru.kabylin.andrey.parking.models

import android.content.Context
import ru.kabylin.andrey.parking.ext.DateFormats
import ru.kabylin.andrey.parking.ext.toString
import java.util.*

data class DateFieldValue(
    val value: Date?,
    val format: DateFormats = DateFormats.HUMANIZE_DATE
) : FieldValue {

    companion object {
        val empty = DateFieldValue(null)
    }

    override fun isFilled() = value != null

    override fun toString(context: Context) = if (isFilled()) {
        value!!.toString(format)
    } else {
        "Не задано"
    }
}
