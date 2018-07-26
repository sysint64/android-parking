package ru.kabylin.andrey.parking.services.http

import com.google.android.gms.location.places.Place
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kabylin.andrey.parking.client.http.HttpClient
import ru.kabylin.andrey.parking.models.Location
import ru.kabylin.andrey.parking.services.PlacesService
import ru.kabylin.andrey.parking.services.http.models.PlaceDetailsResponse
import ru.kabylin.andrey.parking.services.http.models.fromPlaceDetailsResponseToLocation

class HttpPlacesService(private val client: HttpClient, private val apiKey: String) : PlacesService {

    interface ApiGateway {
        @GET("/maps/api/place/details/json")
        fun details(
            @Query("language") language: String,
            @Query("placeid") placeId: String,
            @Query("key") apiKey: String
        ): Single<PlaceDetailsResponse>
    }

    private val apiGateway by lazy {
        client.createRetrofitGateway(
            ApiGateway::class.java,
            HttpClient.Dest.GOOGLE_MAPS
        )
    }

    override fun getLocationForPlace(place: Place): Single<Location> =
        apiGateway.details(language = "ru", placeId = place.id, apiKey = apiKey)
            .map(::fromPlaceDetailsResponseToLocation)
}
