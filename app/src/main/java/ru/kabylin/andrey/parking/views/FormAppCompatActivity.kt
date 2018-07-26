package ru.kabylin.andrey.parking.views

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.find
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.client.*
import ru.kabylin.andrey.parking.ext.hideView
import ru.kabylin.andrey.parking.ext.showView
import ru.kabylin.andrey.parking.forms.BundleFormEncoder
import ru.kabylin.andrey.parking.forms.DefaultFieldDecorator
import ru.kabylin.andrey.parking.forms.DefaultFormInflater
import ru.kabylin.andrey.parking.forms.Form

abstract class FormAppCompatActivity<T : ClientViewState> : ClientAppCompatActivity<T>() {
    val form by lazy {
        val form = Form(this)
        form.onSubmitSubject.subscribe {
            generalErrorTextView?.hideView()
        }
        form
    }

    abstract val progressBarView: View
    open val generalErrorTextView: TextView? = null
    var savedInstanceState: Bundle? = null
        private set

    @CallSuper
    open fun initForm() {
        form.fields.clear()
    }

    @CallSuper
    protected fun afterFormInit() {
        if (savedInstanceState != null) {
            val encoder = BundleFormEncoder(savedInstanceState!!)
            form.decode(encoder)
        }
    }

    protected fun inflateDefaultForm() {
        val formInflater = DefaultFormInflater(form, viewState)
        formInflater.decorator = DefaultFieldDecorator()
        val formContainer: ViewGroup = find(R.id.formContainer)
        formInflater.inflate(formContainer)
    }

    protected inline fun form(crossinline init: (Form).() -> Unit) {
        form.init()
        afterFormInit()
    }

    override fun onLogicError(error: ClientResponse<LogicError>) {
        val logicErrorMessage = error.payload.reason.toString(this)
        generalErrorTextView?.showView()
        generalErrorTextView?.text = logicErrorMessage
    }

    override fun onClientClearErrors() {
        super.onClientClearErrors()
        generalErrorTextView?.hideView()
    }

    override fun onRequestStateUpdated(requestState: ClientResponse<RequestState>) {
        when (requestState.payload) {
            RequestState.STARTED -> {
                progressBarView.visibility = View.VISIBLE
                form.disableSubmit()
            }
            RequestState.FINISHED -> {
                progressBarView.visibility = View.GONE
                form.enableSubmit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        form.onActivityResult(requestCode, resultCode, data)
    }

    override fun displayError(error: ClientResponse<Throwable?>): Boolean {
        return if (error.payload is LogicError && generalErrorTextView != null) {
            generalErrorTextView?.text = error.payload.reason.toString(this)
            true
        } else {
            super.displayError(error)
        }
    }

    override fun onValidationErrors(error: ClientResponse<ValidationErrors>) {
        super.onValidationErrors(error)
        form.setErrors(error.payload.errors.toMutableMap())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val encoder = BundleFormEncoder(outState)
        form.encode(encoder)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        afterFormInit()
    }
}
