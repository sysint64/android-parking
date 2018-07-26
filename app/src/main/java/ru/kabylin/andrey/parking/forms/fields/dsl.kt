package ru.kabylin.andrey.parking.forms.fields

import ru.kabylin.andrey.parking.forms.Field
import ru.kabylin.andrey.parking.forms.FieldPresenter
import ru.kabylin.andrey.parking.forms.Form
import ru.kabylin.andrey.parking.forms.validators.Validator

fun <T: FieldPresenter> Form.field(name: String, validators: List<Validator> = listOf(), presenterFactory: () -> T, init: (T).() -> Unit): T {
    val presenter = presenterFactory()
    val field = Field(name, presenter, isInit = false)

    addField(field)
    field.validators = validators
    presenter.init()
    field.isInit = true
    return presenter
}

fun Form.editText(name: String, validators: List<Validator> = listOf(), init: (TextInputFieldPresenter).() -> Unit): TextInputFieldPresenter =
    field(name, validators, { TextInputFieldPresenter() }, init)

fun Form.location(name: String, validators: List<Validator> = listOf(), init: (LocationFieldPresenter).() -> Unit): LocationFieldPresenter =
    field(name, validators, { LocationFieldPresenter() }, init)
