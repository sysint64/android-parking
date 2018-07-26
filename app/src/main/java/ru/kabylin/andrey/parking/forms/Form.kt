package ru.kabylin.andrey.parking.forms

import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import ru.kabylin.andrey.parking.views.watchers.inputTextWatch

typealias OnSubmitListener = () -> Unit
typealias FormErrorsWatcher = (Map<String, String>?) -> Unit
typealias FormValuesWatcher = (Map<String, Any>?) -> Unit

open class Form(val context: Context) {
    companion object {
        const val FORM_RESULT_CODE_ACC = 100
    }

    internal val fields: MutableList<Field> = ArrayList()
    private val submitButtons: MutableList<Pair<View, OnSubmitListener?>> = ArrayList()
    internal val errors: MutableMap<String, String> = HashMap()
    var errorsWatcher: FormErrorsWatcher? = null
    var valuesWatcher: FormValuesWatcher? = null
    val onSubmitSubject: PublishSubject<Unit> = PublishSubject.create()
    val refreshEvent: PublishSubject<Field> = PublishSubject.create()
    var onSubmit: OnSubmitListener? = null

    val values: MutableMap<String, Any> = HashMap()

    init {
        refreshEvent.subscribe {
            checkRules()

            for (field in fields)
                values[field.name] = field.value

            valuesWatcher?.invoke(values)
        }
    }

    fun checkRules() {
        for (field in fields)
            field.presenter.checkRules()
    }

    fun addField(field: Field) {
        if (field in fields)
            return

        fields.add(field)
        field.form = this
    }

    fun isValid(): Boolean {
        var result = true

        fields
            .asSequence()
            .map { it.isValid() }
            .forEach { result = result && it }

        return result
    }

    fun getValue(field: String): Any {
        return fields.first { it.name == field }.value
    }

    fun getField(field: String): Field {
        return fields.first { it.name == field }
    }

    fun setErrorForField(field: String, @StringRes error: Int) {
        setErrorForField(field, context.getString(error))
    }

    fun setErrorForField(field: String, error: String) {
        fields
            .filter { it.name == field }
            .forEach {
                it.setError(error)
                errors[field] = error
            }

        errorsWatcher?.invoke(this.errors)
    }

    fun setValueForField(field: String, value: Any) {
        fields
            .filter { it.name == field }
            .forEach {
                it.presenter.value = value
            }
    }

    fun setErrors(errors: Map<String, String>) {
        errors.forEach { setErrorForField(it.key, it.value) }
        errorsWatcher?.invoke(this.errors)
    }

    fun setValues(values: Map<String, Any>) {
        values.forEach { setValueForField(it.key, it.value) }
    }

    fun disable() {
        fields.forEach { it.presenter.setEnabled(false) }
        submitButtons.forEach { it.first.isEnabled = false }
    }

    fun enable() {
        fields.forEach { it.presenter.setEnabled(true) }
        submitButtons.forEach { it.first.isEnabled = true }
    }

    fun disableSubmit() {
        submitButtons.forEach { it.first.isEnabled = false }
    }

    fun enableSubmit() {
        submitButtons.forEach { it.first.isEnabled = true }
    }

    fun show() {
        fields.forEach { it.presenter.setVisible(true) }
        submitButtons.forEach { it.first.visibility = View.VISIBLE }
    }

    fun hide() {
        fields.forEach { it.presenter.setVisible(false) }
        submitButtons.forEach { it.first.visibility = View.GONE }
    }

    fun attachSubmitButton(submitButton: View, onSubmitListener: OnSubmitListener? = null) {
        submitButtons.add(Pair(submitButton, onSubmitListener))
        submitButton.setOnClickListener { submit(onSubmitListener) }
    }

    fun attachSubmitInput(codeInput: TextView, onSubmitListener: OnSubmitListener? = null) {
        codeInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit(onSubmitListener)
                true
            } else {
                false
            }
        })
    }

    fun attachSubmitInputOnTextEnter(codeInput: TextView, targetLength: Int, onSubmitListener: OnSubmitListener? = null) {
        val textWatcher by inputTextWatch {
            if (it.length >= targetLength)
                submit(onSubmitListener)
        }
        codeInput.addTextChangedListener(textWatcher)
    }

    fun submit(onSubmitListener: OnSubmitListener? = null) {
        if (isValid()) {
            onSubmit?.invoke()
            onSubmitSubject.onNext(Unit)
            onSubmitListener?.invoke()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (field in fields)
            field.presenter.onActivityResult(requestCode, resultCode, data)
    }

    fun encode(encoder: FormEncoder) {
        for (field in fields)
            encoder.encode(field)
    }

    fun decode(encoder: FormEncoder) {
        for (field in fields)
            encoder.decode(field)
    }
}

// DSL
fun Context.form(init: (Form).() -> Unit): Form {
    val form = Form(this)
    form.init()
    return form
}
