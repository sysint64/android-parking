package ru.kabylin.andrey.parking.router

import android.content.Context
import ru.kabylin.andrey.parking.forms.Form
import ru.kabylin.andrey.parking.views.CommonScreens
import ru.kabylin.andrey.parking.views.ErrorScreenTransition
import ru.kabylin.andrey.parking.views.ScreenTransition

open class Router(protected val context: Context) : RouterAware {
    companion object {
        const val REQUEST_FOR_RESULT_SELECT_ADDRESS_ON_MAP = 1001 + Form.FORM_RESULT_CODE_ACC
    }

    val errorsRouter = ErrorsRouter(context)

    override fun transitionUpdate(screenTransition: ScreenTransition<*>?) {
        val transition = screenTransition?.transition

        when (transition) {
            is CommonScreens -> commonScreenTransition(screenTransition, transition)
            is ErrorScreenTransition -> errorsRouter.errorScreenTransition(screenTransition, transition)
        }
    }

    private fun commonScreenTransition(screenTransition: ScreenTransition<*>, transition: CommonScreens) {
    }
}
