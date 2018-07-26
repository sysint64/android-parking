package ru.kabylin.andrey.parking.forms.fields

import android.view.View
import android.view.ViewGroup
import ru.kabylin.andrey.parking.forms.Field

interface FieldDecorator {
    val handleLabelVisibility: MutableMap<Field, Boolean>
    val handleErrorVisibility: MutableMap<Field, Boolean>

    fun getRootView(): View?

    fun inflate(field: Field, container: ViewGroup) : ViewGroup

    fun refresh(field: Field)

    fun setLabel(field: Field)

    fun setError(field: Field, error: String) {
        field.presenter.setError(error)
    }

    fun clearError(field: Field) {
        field.presenter.clearError()
    }

    fun setEnabled(field: Field, enabled: Boolean) {}
}
