package ru.kabylin.andrey.parking.services.http.models

import ru.kabylin.andrey.parking.client.AccessError
import ru.kabylin.andrey.parking.client.AccessErrorReason
import ru.kabylin.andrey.parking.client.CredentialsError
import ru.kabylin.andrey.parking.models.Location

data class PlaceDetailsResponse(
    val result: PlaceDetailsResultResponse?,
    val status: String
)

data class PlaceDetailsResultResponse(
    val address_components: List<PlaceAddressComponentResponse>?,
    val formatted_address: String?,
    val geometry: PlaceGeometryResponse?
) {
    fun findComponentsByType(type: String): List<PlaceAddressComponentResponse> =
        address_components
            ?.filter { it.types.any { it == type } } ?: listOf()
}

data class PlaceAddressComponentResponse(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class PlaceGeometryResponse(
    val location: PlaceLocationResponse
)

data class PlaceLocationResponse(
    val lat: Double,
    val lng: Double
)

fun fromPlaceDetailsResponseToLocation(response: PlaceDetailsResponse): Location =
    safeTransform {
        when (response.status) {
            "OK" -> fromPlaceDetailsResultResponseToLocation(response.result!!)
            "UNKNOWN_ERROR" -> throw AccessError(AccessErrorReason.BAD_RESPONSE)
            "ZERO_RESULTS" -> throw AccessError(AccessErrorReason.NOT_FOUND)
            "NOT_FOUND" -> throw AccessError(AccessErrorReason.NOT_FOUND)
            "OVER_QUERY_LIMIT" -> throw AccessError(AccessErrorReason.TOO_MANY_REQUESTS)
            "REQUEST_DENIED" -> throw CredentialsError("Google places request denied")
            "INVALID_REQUEST" -> throw AccessError(AccessErrorReason.BAD_RESPONSE)
            else -> throw AccessError(AccessErrorReason.BAD_RESPONSE)
        }
    }

fun fromPlaceDetailsResultResponseToLocation(response: PlaceDetailsResultResponse): Location =
    safeTransform {
        val city = response.findComponentsByType("locality").firstOrNull()?.long_name

        val details = HashMap<Location.DetailsComponent, String>()
        response.formatted_address?.let { details[Location.DetailsComponent.ADDRESS] = it }

        if (city != null)
            details[Location.DetailsComponent.CITY] = city

        Location(
            lat = response.geometry?.location?.lat,
            lng = response.geometry?.location?.lng,
            details = details
        )
    }
