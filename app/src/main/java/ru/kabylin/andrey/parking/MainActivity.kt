package ru.kabylin.andrey.parking

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_default_form.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import ru.kabylin.andrey.parking.client.Client
import ru.kabylin.andrey.parking.router.Router
import ru.kabylin.andrey.parking.services.ParkingService
import ru.kabylin.andrey.parking.views.ClientViewState
import ru.kabylin.andrey.parking.views.FormAppCompatActivity
import ru.kabylin.andrey.parking.views.attachToActivity

class MainActivity : FormAppCompatActivity<ClientViewState>(), KodeinAware {
    override val kodeinContext = kcontext(this)
    override val kodein by closestKodein()

    override val router = Router(this)
    override val viewState by lazy { ClientViewState(client, this) }
    override val client: Client by instance()

    override val progressBarView: View by lazy { progressBar }

    private val parkingService: ParkingService by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_form)

        toolbar.attachToActivity(this, displayHomeButton = true)
        errorsView.attach(container)
    }

    override fun initForm() {
        super.initForm()

        form {

        }

        inflateDefaultForm()
    }
}
