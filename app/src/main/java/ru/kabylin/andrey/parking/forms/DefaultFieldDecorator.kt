package ru.kabylin.andrey.parking.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.ext.hideView
import ru.kabylin.andrey.parking.ext.showView
import ru.kabylin.andrey.parking.forms.fields.FieldDecorator

class DefaultFieldDecorator : FieldDecorator {
    private lateinit var rootView: View

    override fun getRootView(): View? = rootView

    private val containers: MutableMap<Field, View> = HashMap()

    override val handleLabelVisibility: MutableMap<Field, Boolean> = HashMap()
    override val handleErrorVisibility: MutableMap<Field, Boolean> = HashMap()

    private fun isHandleLabelVisibility(field: Field): Boolean {
        val handle = handleLabelVisibility[field]
        return handle == null || handle
    }

    private fun isHandleErrorVisibility(field: Field): Boolean {
        val handle = handleErrorVisibility[field]
        return handle == null || handle
    }

    override fun inflate(field: Field, container: ViewGroup): ViewGroup {
        val inflater = LayoutInflater.from(container.context)
        rootView = inflater.inflate(R.layout.item_default_form_field_decorator, container, false)
        container.addView(rootView)
        containers[field] = rootView
        setLabel(field)
        return rootView.findViewById(R.id.container)
    }

    override fun refresh(field: Field) {
        setLabel(field)
    }

    override fun setLabel(field: Field) {
        val labelTextView = containers[field]!!.findViewById(R.id.labelTextView) as TextView
        labelTextView.text = field.label

        if (field.label.isNotBlank() && isHandleLabelVisibility(field)) {
            labelTextView.text = field.label
        } else {
            labelTextView.hideView()
        }
    }

    override fun setError(field: Field, error: String) {
        if (!isHandleErrorVisibility(field))
            return

        val errorTextView = containers[field]!!.findViewById(R.id.errorTextView) as TextView
        errorTextView.text = error
        errorTextView.showView()
    }

    override fun clearError(field: Field) {
        if (!isHandleErrorVisibility(field))
            return

        val errorTextView = containers[field]!!.findViewById(R.id.errorTextView) as TextView
        errorTextView.text = ""
        errorTextView.hideView()
    }

    override fun setEnabled(field: Field, enabled: Boolean) {
        containers[field]!!.isEnabled = enabled
        containers[field]!!.alpha = if (enabled) 1f else 0.5f
    }
}
