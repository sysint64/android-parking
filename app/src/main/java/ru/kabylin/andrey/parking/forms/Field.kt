package ru.kabylin.andrey.parking.forms

import ru.kabylin.andrey.parking.forms.validators.Validator

class Field(
    val name: String,
    internal val presenter: FieldPresenter,
    internal var isInit: Boolean = true
) {
    lateinit var form: Form

    init {
        presenter.field = this
    }

    var validators: List<Validator> = listOf()
        set (value) {
            field = value

            for (validator in value)
                validator.resources = form.context.resources
        }

    var label: String = ""
        set (value) {
            field = value

            if (isInit)
                form.refreshEvent.onNext(this)
        }

    val value: Any
        get() = presenter.value

    val hasError: Boolean
        get() = form.errors.containsKey(name)

    fun isValid(): Boolean {
        val value = presenter.value
        clearError()

        for (validator in validators) {
            val (isHookType, isValid) = validator.isHooksValid(value)
            val validatorIsValid = if (isHookType) isValid else validator.isValid(value)

            if (validatorIsValid)
                continue

            /// Set error text
            val (isHookErrorTextType, errorString) = validator.isErrorTextHook(value)

            if (isHookErrorTextType) {
                setError(errorString)
            } else {
                setError(validator.getErrorText(value))
            }
            return false
        }

        return true
    }

    fun setError(error: String) {
        form.errors[name] = error
        form.errorsWatcher?.invoke(form.errors)

        with (presenter.decorator) {
            if (this != null) {
                setError(this@Field, error)
            } else {
                presenter.setError(error)
            }
        }
    }

    fun clearError() {
        form.errors.remove(name)
        form.errorsWatcher?.invoke(form.errors)

        with (presenter.decorator) {
            if (this != null) {
                clearError(this@Field)
            } else {
                presenter.clearError()
            }
        }
    }
}
