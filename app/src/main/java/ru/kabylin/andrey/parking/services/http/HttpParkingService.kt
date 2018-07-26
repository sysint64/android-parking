package ru.kabylin.andrey.parking.services.http

import io.reactivex.Completable
import ru.kabylin.andrey.parking.client.http.HttpClient
import ru.kabylin.andrey.parking.services.ParkingService

class HttpParkingService(private val client: HttpClient) : ParkingService {
    override fun addParking(parkingInfo: ParkingService.ParkingInfo): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
