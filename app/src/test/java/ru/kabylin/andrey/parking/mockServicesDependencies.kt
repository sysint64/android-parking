package ru.kabylin.andrey.parking

import io.mockk.mockk
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import ru.kabylin.andrey.parking.services.ParkingService
import ru.kabylin.andrey.parking.services.PlacesService

val mockServicesDependencies = Kodein.Module {
    bind<PlacesService>(overrides = true) with singleton { mockk<PlacesService>(relaxed = true) }
    bind<ParkingService>(overrides = true) with singleton { mockk<ParkingService>(relaxed = true) }
}
