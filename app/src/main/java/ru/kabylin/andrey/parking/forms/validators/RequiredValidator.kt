package ru.kabylin.andrey.parking.forms.validators

import android.content.res.Resources
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.forms.Fillable

class RequiredValidator(resources: Resources? = null) : Validator(resources) {
    override fun getErrorText(value: Any?): String =
        errorFromRes(R.string.required_error)

    override fun isValid(value: Any?): Boolean =
        when (value) {
            value == null -> false
            is String -> !value.isBlank()
            is Boolean -> value
            is Fillable -> value.isFilled()
            else -> throw UnsupportedOperationException()
        }
}
