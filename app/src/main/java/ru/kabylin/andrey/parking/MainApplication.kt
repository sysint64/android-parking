package ru.kabylin.andrey.parking

import android.app.Application
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import ru.kabylin.andrey.parking.client.Client
import ru.kabylin.andrey.parking.compositors.Compositor

class MainApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(dependencies(this@MainApplication))
    }

    override fun onCreate() {
        super.onCreate()

        val client: Client by instance()
        val clientCompositor: Compositor by instance("client")
        client.compositor = clientCompositor
    }
}
