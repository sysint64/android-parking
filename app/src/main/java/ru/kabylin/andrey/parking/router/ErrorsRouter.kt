package ru.kabylin.andrey.parking.router

import android.content.Context
import org.jetbrains.anko.alert
import ru.kabylin.andrey.parking.views.ErrorScreenTransition
import ru.kabylin.andrey.parking.views.ScreenTransition

class ErrorsRouter(private val context: Context)  {
    fun errorScreenTransition(transition: ScreenTransition<*>, errorType: ErrorScreenTransition?) {
        when (errorType) {
            ErrorScreenTransition.CRITICAL_ERROR ->
                context.alert("Неизвестная ошибка!").show()

            ErrorScreenTransition.VERSION_ERROR ->
                context.alert("Версия приложения устарела!").show()
        }
    }
}
