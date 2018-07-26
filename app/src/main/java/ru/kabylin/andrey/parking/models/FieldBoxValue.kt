package ru.kabylin.andrey.parking.models

import android.content.Context

data class FieldBoxValue<out T>(val value: T?) : FieldValue, BoxValue<T> {
    companion object {
        val empty = FieldBoxValue(null)
    }

    override fun isFilled() = value != null

    override fun toString(context: Context) = if (isFilled()) {
        value!!.toString()
    } else {
        "Не задано"
    }

    override fun unbox(): T? = value
}
