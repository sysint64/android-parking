package ru.kabylin.andrey.parking.services.http.models

import junit.framework.Assert.fail
import org.junit.Test
import ru.kabylin.andrey.parking.client.AccessError
import ru.kabylin.andrey.parking.client.AccessErrorReason
import junit.framework.Assert.assertEquals
import ru.kabylin.andrey.parking.BaseTest
import ru.kabylin.andrey.parking.client.CredentialsError
import ru.kabylin.andrey.parking.models.Location

class PlaceDetailsResponseTest : BaseTest() {
    private fun getPlaceDetailsResponse(status: String): PlaceDetailsResponse =
        when (status) {
            "OK" -> loadJson(
                "services/google_maps/places_details_status_${status.toLowerCase()}.json",
                PlaceDetailsResponse::class.java
            )
            else -> loadJson(
                "services/google_maps/status_${status.toLowerCase()}.json",
                PlaceDetailsResponse::class.java
            )
        }

    @Test
    fun fromPlaceDetailsResponseToLocation_status_OK() {
        val response = getPlaceDetailsResponse("OK")
        val location = fromPlaceDetailsResponseToLocation(response)

        assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", location.details[Location.DetailsComponent.ADDRESS])
        assertEquals("Pyrmont", location.details[Location.DetailsComponent.CITY])
        assertEquals(-33.866651, location.lat)
        assertEquals(151.195827, location.lng)
    }

    private fun expectAccessError(reason: AccessErrorReason, action: () -> Unit) {
        try {
            action()
            fail("Expected an AccessError to be thrown");
        } catch (accessError: AccessError) {
            assertEquals(reason, accessError.reason)
        }
    }

    @Test
    fun fromPlaceDetailsResponseToLocation_status_UNKNOWN_ERROR() {
        val response = getPlaceDetailsResponse("UNKNOWN_ERROR")

        expectAccessError(AccessErrorReason.BAD_RESPONSE) {
            fromPlaceDetailsResponseToLocation(response)
        }
    }

    @Test
    fun fromPlaceDetailsResponseToLocation_status_ZERO_RESULTS() {
        val response = getPlaceDetailsResponse("ZERO_RESULTS")

        expectAccessError(AccessErrorReason.NOT_FOUND) {
            fromPlaceDetailsResponseToLocation(response)
        }
    }

    @Test
    fun fromPlaceDetailsResponseToLocation_status_OVER_QUERY_LIMIT() {
        val response = getPlaceDetailsResponse("OVER_QUERY_LIMIT")

        expectAccessError(AccessErrorReason.TOO_MANY_REQUESTS) {
            fromPlaceDetailsResponseToLocation(response)
        }
    }

    @Test(expected = CredentialsError::class)
    fun fromPlaceDetailsResponseToLocation_status_REQUEST_DENIED() {
        val response = getPlaceDetailsResponse("REQUEST_DENIED")
        fromPlaceDetailsResponseToLocation(response)
    }

    @Test
    fun fromPlaceDetailsResponseToLocation_status_INVALID_REQUEST() {
        val response = getPlaceDetailsResponse("INVALID_REQUEST")

        expectAccessError(AccessErrorReason.BAD_RESPONSE) {
            fromPlaceDetailsResponseToLocation(response)
        }
    }

    @Test
    fun fromPlaceDetailsResponseToLocation_status_NOT_FOUND() {
        val response = getPlaceDetailsResponse("NOT_FOUND")

        expectAccessError(AccessErrorReason.NOT_FOUND) {
            fromPlaceDetailsResponseToLocation(response)
        }
    }
}
