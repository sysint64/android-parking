package ru.kabylin.andrey.parking.forms.validators

import android.content.res.Resources
import ru.kabylin.andrey.parking.R

class EmailValidator(resources: Resources? = null): Validator(resources) {
    override fun getErrorText(value: Any?): String =
        errorFromRes(R.string.email_validation_error)

    override fun isValid(value: Any?): Boolean =
        when (value) {
            is CharSequence -> android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() || value.isBlank()
            else -> throw UnsupportedOperationException()
        }
}
