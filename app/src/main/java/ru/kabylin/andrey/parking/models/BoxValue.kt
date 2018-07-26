package ru.kabylin.andrey.parking.models

interface BoxValue<out T> {
    fun unbox(): T?
}
