package ru.kabylin.andrey.parking.router

import ru.kabylin.andrey.parking.views.ScreenTransition

interface RouterAware {
    fun transitionUpdate(screenTransition: ScreenTransition<*>?) {}
}
