package ru.kabylin.andrey.parking.forms.validators

import android.content.res.Resources
import ru.kabylin.andrey.parking.R

class NumberValidator(resources: Resources? = null): Validator(resources) {
    override fun getErrorText(value: Any?): String =
        errorFromRes(R.string.int_validation_error)

    override fun isValid(value: Any?): Boolean =
        when (value) {
            is CharSequence ->
                value.matches(Regex("""\d+"""))
            else ->
                throw UnsupportedOperationException()
        }
}
