package ru.kabylin.andrey.parking.client.http

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.kabylin.andrey.parking.compositors.ConfigCompositor
import ru.kabylin.andrey.parking.compositors.MergeCompositor
import ru.kabylin.andrey.parking.compositors.SchedulerCompositor

class HttpClientCompositor(val client: HttpClient) : ConfigCompositor() {

    override val config = MergeCompositor(
        SchedulerCompositor(
            Schedulers.single(),
            AndroidSchedulers.mainThread()
        ),
        HttpErrorsCompositor(client)
    )
}
