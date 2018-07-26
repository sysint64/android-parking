package ru.kabylin.andrey.parking

import com.google.gson.Gson
import java.io.InputStream

abstract class BaseTest {
    protected fun loadResourceAsStream(path: String): InputStream {
        println("ru/kabylin/andrey/parking/$path")
        return javaClass.classLoader.getResourceAsStream("ru/kabylin/andrey/parking/$path")
    }

    protected fun <T> loadJson(path: String, class_: Class<T>): T {
        val gson = Gson()
        val stream = loadResourceAsStream(path)

        return gson.fromJson(stream.reader(), class_)
    }
}
