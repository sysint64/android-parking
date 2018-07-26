package ru.kabylin.andrey.parking.forms.fields

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.forms.FieldPresenter
import ru.kabylin.andrey.parking.router.Router
import ru.kabylin.andrey.parking.views.CommonScreens
import ru.kabylin.andrey.parking.views.ViewState
import com.google.android.gms.location.places.ui.PlacePicker
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import ru.kabylin.andrey.parking.client.ClientAware
import ru.kabylin.andrey.parking.client.DescReason
import ru.kabylin.andrey.parking.client.LogicError
import ru.kabylin.andrey.parking.ext.*
import ru.kabylin.andrey.parking.models.Location
import ru.kabylin.andrey.parking.services.PlacesService

open class LocationFieldPresenter : FieldPresenter() {
    protected var selected: Location = Location.empty
    private lateinit var addressTextView: TextView
    var readOnly = false
    private var progressView: View? = null
    private lateinit var trigger: View

    override var value: Any
        get() = selected
        set(value) {
            select(value as Location)
        }

    fun attach(viewState: ViewState, trigger: View, addressTextView: TextView, progressView: View? = null) {
        this.addressTextView = addressTextView
        this.progressView = progressView
        this.trigger = trigger

        select(selected)

        if (readOnly) {
            if (trigger is TextView) {
                trigger.setText(R.string.view_on_map)
            }
        }

        trigger.setOnClickListener {
            if (readOnly) {
//                val bundle = selected.createLocationBundle()
//                viewState.gotoScreen(CommonScreens.LOCATION_ON_MAP, bundle)
            } else {
                if (progressView != null) {
                    progressView.showView()
                    trigger.invisibleView()
                    trigger.disable()
                }

                val bundle = createBundle(mapOf("lat" to selected.lat, "lng" to selected.lng))

                (field.form.context as Activity).permissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribeOnNext {
                        if (it) {
                            viewState.gotoScreen(CommonScreens.SELECT_ADDRESS_ON_MAP, bundle)
                        } else {
                            viewState.gotoScreen(CommonScreens.SELECT_ADDRESS_ON_MAP, bundle)
                        }
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (progressView != null) {
            progressView?.invisibleView()
            trigger.showView()
            trigger.enable()
        }

        if (resultCode != Activity.RESULT_OK || data == null)
            return

        if (requestCode == Router.REQUEST_FOR_RESULT_SELECT_ADDRESS_ON_MAP) {
            val place = PlacePicker.getPlace(field.form.context, data)
            val client = (field.form.context as ClientAware).client
            val placesService : PlacesService by (field.form.context as KodeinAware).instance()
            val query = placesService.getLocationForPlace(place)

            client.execute(query) {
                select(it.payload)

                if (!it.payload.isFilled())
                    client.onError(LogicError(DescReason.string("Не удалось распознать место\nПопробуйте выбрать что-то другое")))
            }
        }
    }

    protected open fun select(location: Location) {
        if (::addressTextView.isInitialized) {
            if (location.isFilled()) {
                addressTextView.text = location.toString(field.form.context)
            } else {
                addressTextView.setText(R.string.select_address_description)
            }
        }

        selected = location
        emitRefreshEvent()
    }
}
