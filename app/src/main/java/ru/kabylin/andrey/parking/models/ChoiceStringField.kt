package ru.kabylin.andrey.parking.models

import android.content.Context
import java.io.Serializable

data class ChoiceStringField(
    val key: String,
    val value: String?
) : FieldValue, Serializable {

    companion object {
        val empty = ChoiceStringField("", null)
    }

    override fun toString(context: Context): String =
        if (isFilled()) value.toString() else "Не задано"

    override fun isFilled(): Boolean =
        value != null && value.isNotBlank()
}
