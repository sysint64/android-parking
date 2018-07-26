package ru.kabylin.andrey.parking.forms

interface Fillable {
    fun isFilled(): Boolean
}

class FillableBox<T>(val value: T?) : Fillable {
    override fun isFilled() = value != null

    fun unbox(): T? = value
}
