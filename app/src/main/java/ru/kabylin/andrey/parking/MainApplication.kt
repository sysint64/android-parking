package ru.kabylin.andrey.parking

import android.app.Application
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class MainApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(dependencies(this@MainApplication))
    }
}
