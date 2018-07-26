package ru.kabylin.andrey.parking.forms

import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.forms.fields.*
import ru.kabylin.andrey.parking.views.ViewState

open class DefaultFormInflater(form: Form, val viewState: ViewState? = null) : FormInflater(form) {
    override fun inflateField(presenter: FieldPresenter, container: ViewGroup): View {
        return when (presenter) {
            is LocationFieldPresenter -> inflateLocation(presenter, container)
            is TextInputFieldPresenter -> inflateTextInput(presenter, container)
            else -> throw UnsupportedOperationException("Unsupported field type ${presenter::class}")
        }
    }

    private fun inflateLocation(presenter: LocationFieldPresenter, container: ViewGroup): View {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.item_form_field_location, container, false)

        presenter.attach(viewState!!,
            view.findViewById(R.id.selectButton) as Button,
            view.findViewById(R.id.addressTextView) as TextView,
            view.findViewById(R.id.locationProgressBar) as View
        )

        return view
    }

    private fun inflateTextInput(presenter: TextInputFieldPresenter, container: ViewGroup): View {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.item_form_field_text_input, container, false)
        val editText = view.findViewById(R.id.editText) as AppCompatEditText
        presenter.attach(editText)
        return view
    }
}
