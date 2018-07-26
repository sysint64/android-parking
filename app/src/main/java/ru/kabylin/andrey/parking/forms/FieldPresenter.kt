package ru.kabylin.andrey.parking.forms

import android.content.Intent
import android.support.annotation.CallSuper
import android.view.View
import io.reactivex.subjects.PublishSubject
import ru.kabylin.andrey.parking.forms.fields.FieldDecorator

abstract class FieldPresenter {
    var decoratorContainer: View? = null

    lateinit var field: Field
        internal set

    val onValueChangeEvent: PublishSubject<Field> = PublishSubject.create()

    var attachedViews = false
        protected set

    fun refresh() {
        setEnabled(true)
        decorator?.refresh(field)
    }

    var decorator: FieldDecorator? = null

    abstract var value: Any

    internal open fun setError(error: String) {}

    internal open fun clearError() {}

    private var isEnabled: Boolean = true
    private var isVisible: Boolean = true

    @CallSuper
    open fun setVisible(isVisible: Boolean) {
        decoratorContainer?.visibility = if (isVisible) View.VISIBLE else View.GONE
        this.isVisible = isVisible
    }

    @CallSuper
    open fun setEnabled(isEnabled: Boolean) {
        decorator?.setEnabled(this.field, isEnabled)
        this.isEnabled = isEnabled
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    private val visibilityRules: MutableList<() -> Boolean> = ArrayList()
    private val enableRules: MutableList<() -> Boolean> = ArrayList()

    fun addVisibilityRule(rule: () -> Boolean) {
        visibilityRules.add(rule)
    }

    fun addEnableRule(rule: () -> Boolean) {
        enableRules.add(rule)
    }

    fun checkRules() {
        for (rule in visibilityRules)
            setVisible(rule())

        for (rule in enableRules)
            setEnabled(rule())
    }

    protected fun emitRefreshEvent() {
        onValueChangeEvent.onNext(field)
        field.form.refreshEvent.onNext(field)
    }
}
