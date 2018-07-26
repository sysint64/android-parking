package ru.kabylin.andrey.parking.services

import io.reactivex.Completable
import ru.kabylin.andrey.parking.models.Location
import ru.kabylin.andrey.parking.models.Photo

interface ParkingService {
    data class ParkingInfo(
        val location: Location,
        val phone: String,
        val comment: String,
        val photo: Photo
    )

    fun addParkingPlace(parkingInfo: ParkingInfo): Completable
}
