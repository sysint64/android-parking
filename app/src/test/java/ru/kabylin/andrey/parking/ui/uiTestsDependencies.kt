package ru.kabylin.andrey.parking.ui

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import ru.kabylin.andrey.parking.compositors.Compositor
import ru.kabylin.andrey.parking.compositors.EmptyCompositor
import ru.kabylin.andrey.parking.dependencies
import ru.kabylin.andrey.parking.mockServicesDependencies

fun uiTestsDependencies(context: Context) = Kodein.Module {
    import(dependencies(context))
    bind<Compositor>(tag = "client", overrides = true) with singleton { EmptyCompositor() }
    import(mockServicesDependencies, allowOverride = true)
}
