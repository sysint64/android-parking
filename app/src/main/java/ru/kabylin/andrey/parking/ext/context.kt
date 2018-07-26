package ru.kabylin.andrey.parking.ext

import android.content.Context
import android.util.TypedValue

fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}
