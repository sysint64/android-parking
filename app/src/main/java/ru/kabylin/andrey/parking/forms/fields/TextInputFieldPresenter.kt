package ru.kabylin.andrey.parking.forms.fields

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.view.View
import android.widget.EditText
import android.widget.TextView
import ru.kabylin.andrey.parking.ext.hideView
import ru.kabylin.andrey.parking.ext.showView
import ru.kabylin.andrey.parking.forms.FieldPresenter
import ru.kabylin.andrey.parking.views.watchers.TextWatcherChanged

class TextInputFieldPresenter : FieldPresenter() {
    protected lateinit var editText: AppCompatEditText
    private var textInputLayout: TextInputLayout? = null
    private var errorTextView: TextView? = null
    private var text: String = ""

    var hint: String = ""
        set(value) {
            if (textInputLayout != null) {
                textInputLayout?.hint = value
            } else {
                editText.hint = value
            }

            field = value
        }

    override var value: Any
        get() {
            return when {
                !::editText.isInitialized -> text
                else -> editText.text.toString()
            }
        }
        set(value) {
            text = value.toString()

            if (!::editText.isInitialized)
                return

            editText.setText(value.toString())
        }

    fun attach(editText: AppCompatEditText, textInputLayout: TextInputLayout? = null, errorTextView: TextView? = null) {
        this.editText = editText
        this.textInputLayout = textInputLayout
        this.attachedViews = true
        this.editText.setText(text)
        this.errorTextView = errorTextView

        this.editText.addTextChangedListener(
            object : TextWatcherChanged() {
                override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
                    emitRefreshEvent()
                }
            }
        )
    }

    override fun setVisible(isVisible: Boolean) {
        super.setVisible(isVisible)
        val container = textInputLayout ?: editText
        if (isVisible) container.visibility = View.VISIBLE
        else container.visibility = View.INVISIBLE
    }

    override fun setError(error: String) {
        when {
            textInputLayout != null -> textInputLayout!!.error = error
            errorTextView != null -> {
                errorTextView!!.showView()
                errorTextView!!.text = error
            }
            else -> editText.error = error
        }
    }

    override fun clearError() {
        when {
            textInputLayout != null -> textInputLayout!!.error = null
            errorTextView != null -> errorTextView!!.hideView()
            else -> editText.error = null
        }
    }

    fun editTextAttrs(init: (EditText).() -> Unit) {
        editText.init()
    }

    fun textInputLayoutAttrs(init: (TextInputLayout).() -> Unit) {
        textInputLayout?.init()
    }

    override fun setEnabled(isEnabled: Boolean) {
        super.setEnabled(isEnabled)
        textInputLayout?.isEnabled = isEnabled
        editText.isEnabled = isEnabled
    }
}
