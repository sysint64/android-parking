package ru.kabylin.andrey.parking

import android.content.Context
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import ru.kabylin.andrey.parking.client.Client
import ru.kabylin.andrey.parking.client.http.HttpClient
import ru.kabylin.andrey.parking.client.http.HttpClientCompositor
import ru.kabylin.andrey.parking.compositors.Compositor
import ru.kabylin.andrey.parking.services.*
import ru.kabylin.andrey.parking.services.http.HttpParkingService
import ru.kabylin.andrey.parking.services.http.HttpPlacesService

fun dependencies(context: Context) = Kodein.Module {
    val httpClient = HttpClient

    bind<Client>() with singleton { httpClient }
    bind<Compositor>("client") with provider {
        HttpClientCompositor(client = instance<Client>() as HttpClient)
    }

    bind<PlacesService>() with singleton { HttpPlacesService(httpClient, "AIzaSyApPShMK8awEZJOlODR6fWC4m5fdTpgr0g") }
    bind<ParkingService>() with singleton { HttpParkingService(httpClient) }
}
