package ru.kabylin.andrey.parking.ui

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.robolectric.TestLifecycleApplication
import ru.kabylin.andrey.parking.R
import java.lang.reflect.Method

class UiTestApplication : Application(), KodeinAware, TestLifecycleApplication {
    override val kodein =
        Kodein.lazy {
            import(uiTestsDependencies(this@UiTestApplication), allowOverride = true)
        }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }

    override fun beforeTest(method: Method?) {}

    override fun prepareTest(test: Any?) {
    }

    override fun afterTest(method: Method?) {
    }
}
