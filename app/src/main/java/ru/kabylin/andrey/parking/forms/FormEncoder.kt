package ru.kabylin.andrey.parking.forms

interface FormEncoder {
    fun encode(field: Field) {
        encode(field.name, field.presenter.value)
    }

    fun decode(field: Field) {
        val default = field.presenter.value
        decode(field.name, default, default)?.let {
            field.presenter.value = it
        }
    }

    fun encode(name: String, value: Any?)

    fun decode(name: String, value: Any?, default: Any): Any?
}
