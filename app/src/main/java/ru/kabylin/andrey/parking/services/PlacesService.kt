package ru.kabylin.andrey.parking.services

import com.google.android.gms.location.places.Place
import io.reactivex.Single
import ru.kabylin.andrey.parking.models.Location

interface PlacesService {
    fun getLocationForPlace(place: Place): Single<Location>
}
