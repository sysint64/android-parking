package ru.kabylin.andrey.parking.forms.validators

import android.content.res.Resources
import ru.kabylin.andrey.parking.R

class MaxMinStringValidator(resources: Resources? = null, private val min: Int, private val max: Int): Validator(resources) {
    override fun isValid(value: Any?): Boolean {
        if (value !is CharSequence)
            throw UnsupportedOperationException()

        return value.length in min..max
    }

    override fun getErrorText(value: Any?): String {
        if (value !is CharSequence)
            throw UnsupportedOperationException()

        return when {
            value.length < min -> {
                val format = resources!!.getString(R.string.min_error)
                String.format(format, min)
            }
            value.length > max -> {
                val format = resources!!.getString(R.string.max_error)
                String.format(format, max)
            }
            else -> throw UnsupportedOperationException()
        }
    }
}
