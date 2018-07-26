package ru.kabylin.andrey.parking.forms

import android.view.View
import android.view.ViewGroup
import ru.kabylin.andrey.parking.forms.fields.FieldDecorator

abstract class FormInflater(val form: Form) {
    var decorator: FieldDecorator? = null
    val fieldContainers = ArrayList<View>()

    fun inflate(container: ViewGroup) {
        form.fields.forEach {
            if (!it.presenter.attachedViews) {
                it.presenter.decorator = decorator
                val fieldContainer = decorator?.inflate(it, container) ?: container
                val view = inflateField(it.presenter, fieldContainer)
                fieldContainer.addView(view)

                it.presenter.decoratorContainer = decorator?.getRootView()
                it.presenter.refresh()
                fieldContainers.add(fieldContainer)
            }
        }

        form.checkRules()
    }

    abstract fun inflateField(presenter: FieldPresenter, container: ViewGroup): View

    fun clear() {
        for (container in fieldContainers) {
            val parent = container.parent as ViewGroup
            parent.removeView(container)
        }

        fieldContainers.clear()
    }
}
