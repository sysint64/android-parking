package ru.kabylin.andrey.parking.client.http

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.kabylin.andrey.parking.BuildConfig
import ru.kabylin.andrey.parking.client.Client
import java.util.concurrent.TimeUnit

object HttpClient : Client() {
    private val mainApiRetrofit by lazy {
        val endpoint = BuildConfig.API_ENDPOINT
        retrofitBuilder(endpoint)
            .client(createHttpClientInstance())
            .build()
    }

    private val googlePlacesRetrofit by lazy {
        retrofitBuilder("https://maps.googleapis.com")
            .client(createHttpClientInstance())
            .build()
    }

    private fun retrofitBuilder(endpoint: String): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(endpoint)
    }

    private fun createHttpClientInstance(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        return httpClient.build()
    }

    enum class Dest {
        MAIN_API,
        GOOGLE_MAPS,
        ;
    }

    fun <T> createRetrofitGateway(cls: Class<T>, dest: Dest = Dest.MAIN_API): T {
        return when (dest) {
            Dest.MAIN_API -> mainApiRetrofit.create(cls)
            Dest.GOOGLE_MAPS -> googlePlacesRetrofit.create(cls)
        }
    }
}
