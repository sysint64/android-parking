package ru.kabylin.andrey.parking.services

import io.reactivex.Completable
import ru.kabylin.andrey.parking.models.Location

interface ParkingService {
    data class ParkingInfo(
        val location: Location,
        val phone: String,
        val comment: String
    )

    fun addParking(parkingInfo: ParkingInfo): Completable
}
