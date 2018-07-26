package ru.kabylin.andrey.parking.models

import android.content.Context
import java.io.Serializable
import com.google.android.gms.maps.model.LatLng

data class Location(
    val lat: Double?,
    val lng: Double?,
    val details: Map<DetailsComponent, String>
) : FieldValue, Serializable {

    companion object {
        val empty = Location()
    }

    enum class DetailsComponent {
        HOUSE,
        STREET,
        CITY,
        COUNTRY,
        ADDRESS,
        OFFICE
        ;
    }

    val address: String?
        get() = details[DetailsComponent.ADDRESS]

    val city: String?
        get() = details[DetailsComponent.CITY]

    val country: String?
        get() = details[DetailsComponent.COUNTRY]

    val locality: String
        get() {
            return listOfNotNull(city, country)
                .filterNot { it.isBlank() }
                .joinToString(", ")
        }

    internal constructor() : this(null, null, HashMap())

    override fun isFilled(): Boolean =
        lat != null && lng != null && fullAddress().isNotBlank()

    override fun toString(): String {
        return if (!isFilled()) {
            "Не задано"
        } else {
            fullAddress()
        }
    }

    private fun fullAddress(): String {
        if (details[DetailsComponent.ADDRESS] != null)
            return details[DetailsComponent.ADDRESS]!!

        val ordered = listOf(
            details[DetailsComponent.COUNTRY],
            details[DetailsComponent.CITY],
            details[DetailsComponent.STREET],
            details[DetailsComponent.HOUSE],
            details[DetailsComponent.OFFICE]
        )

        return ordered
            .filterNotNull()
            .filter { it.isNotBlank() }
            .joinToString(", ")
    }

    override fun toString(context: Context): String = toString()

    val latLng: LatLng?
        get() {
            return if (isFilled()) {
                LatLng(lat!!, lng!!)
            } else {
                null
            }
        }
}
