package ru.kabylin.andrey.parking.services.http

import android.graphics.Bitmap
import android.util.Base64
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.client.LogicError
import ru.kabylin.andrey.parking.client.http.HttpClient
import ru.kabylin.andrey.parking.services.ParkingService
import java.io.ByteArrayOutputStream

class HttpParkingService(private val client: HttpClient) : ParkingService {
    interface ApiGateway {
        data class AddParkingPlaceRequest(
            val lat: Double,
            val lng: Double,
            val location: String,
            val city: String,
            val phone: String,
            val comment: String,
            val photo_base64: String
        )

        @POST("parking/add")
        fun addParkingPlace(@Body body: AddParkingPlaceRequest): Completable
    }

    private val apiGateway by lazy {
        client.createRetrofitGateway(
            ApiGateway::class.java,
            HttpClient.Dest.MAIN_API
        )
    }

    override fun addParkingPlace(parkingInfo: ParkingService.ParkingInfo): Completable {
        val photoBase64 = getBase64(parkingInfo.photo.bitmap!!)
            ?: return Completable.error(LogicError(R.string.max_upload_size_error))

        val body = ApiGateway.AddParkingPlaceRequest(
            photo_base64 = photoBase64,
            lat = parkingInfo.location.lat!!,
            lng = parkingInfo.location.lng!!,
            location = parkingInfo.location.toString(),
            city = parkingInfo.location.city!!,
            phone = parkingInfo.phone,
            comment = parkingInfo.comment
        )

        return apiGateway.addParkingPlace(body)
    }

    private fun getBase64(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val imageBytes = outputStream.toByteArray()

        if (imageBytes.size > HttpClient.MAX_UPLOAD_SIZE_IN_BYTES)
            return null

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}
