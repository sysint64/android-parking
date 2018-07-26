package ru.kabylin.andrey.parking.models

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcel
import android.support.annotation.DrawableRes

data class Photo(
    val path: String?,
    val bitmap: Bitmap?,
    @DrawableRes val res: Int?
) : FieldValue {

    companion object {
        fun path(string: String): Photo {
            return Photo(string, null, null)
        }

        fun res(@DrawableRes res: Int): Photo {
            return Photo(null, null, res)
        }

        fun bitmap(bitmap: Bitmap): Photo {
            return Photo(null, bitmap, null)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun isFilled(): Boolean {
        return path != null || res != null || bitmap != null
    }

    inline fun <T> apply(
        crossinline path: (value: String) -> T,
        crossinline res: (value: Int) -> T
    ): T = when {
        this.path != null -> path(this.path)
        this.res != null -> res(this.res)
        else -> throw UnsupportedOperationException("Unsupported")
    }

    override fun toString(context: Context): String = "Stub"
}
