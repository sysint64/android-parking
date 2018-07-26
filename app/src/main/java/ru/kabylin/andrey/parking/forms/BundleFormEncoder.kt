package ru.kabylin.andrey.parking.forms

import android.os.Bundle
import ru.kabylin.andrey.parking.models.ChoiceStringField
import ru.kabylin.andrey.parking.models.DateFieldValue
import ru.kabylin.andrey.parking.models.FieldBoxValue
import ru.kabylin.andrey.parking.models.Location
import java.util.*
import kotlin.collections.ArrayList

class BundleFormEncoder(val bundle: Bundle) : FormEncoder {

    override fun encode(name: String, value: Any?) {
        if (value == null)
            throw UnsupportedOperationException("Can't encode null value, please use FieldBoxValue instead")

        when (value) {
            is String -> bundle.putString(name, value)
            is Int -> bundle.putInt(name, value)
            is Double -> bundle.putDouble(name, value)
            is Boolean -> bundle.putBoolean(name, value)
            is FieldBoxValue<*> -> encode(name, value.value)
            is ChoiceStringField -> bundle.putString(name, "${value.key}:${value.value}")
            is DateFieldValue -> bundle.putLong(name, value.value?.time ?: -1L)
            is Location -> bundle.putSerializable(name, value)
            is ArrayList<*> -> bundle.putSerializable(name, value)
            else -> throw UnsupportedOperationException("Unknown value type to encoding: ${value::class}")
        }
    }

    override fun decode(name: String, value: Any?, default: Any): Any {
        if (value == null)
            return default

        return when (value) {
            is String -> bundle.getString(name, value)
            is Int -> bundle.getInt(name, value)
            is Double -> bundle.getDouble(name, value)
            is Boolean -> bundle.getBoolean(name, value)
            is FieldBoxValue<*> -> decode(name, value.value, value)
            is ChoiceStringField -> {
                val serialized: String? = bundle.getString(name, null)
                val pair = serialized?.split(":")

                when {
                    serialized == null -> default
                    pair!!.size != 2 -> default
                    else -> ChoiceStringField(pair[0], pair[1])
                }
            }
            is DateFieldValue -> {
                val time = bundle.getLong(name, -1L)

                if (time < 0L) {
                    default
                } else {
                    DateFieldValue(Date(time))
                }
            }
            is Location -> bundle.getSerializable(name) as Location
            is ArrayList<*> -> bundle.getSerializable(name)
            else -> default
        }
    }
}
