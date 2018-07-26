package ru.kabylin.andrey.parking.ui

import org.robolectric.RuntimeEnvironment

abstract class BaseUiTest {
    protected val application: UiTestApplication
        get() = RuntimeEnvironment.application as UiTestApplication
}