package ru.kabylin.andrey.parking.router

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.jetbrains.anko.longToast
import ru.kabylin.andrey.parking.forms.Form
import ru.kabylin.andrey.parking.views.CommonScreens
import ru.kabylin.andrey.parking.views.ErrorScreenTransition
import ru.kabylin.andrey.parking.views.ScreenTransition

open class Router(protected val context: Context) : RouterAware {
    companion object {
        const val REQUEST_FOR_RESULT_SELECT_ADDRESS_ON_MAP = 1001 + Form.FORM_RESULT_CODE_ACC
    }

    val errorsRouter = ErrorsRouter(context)

    override fun transitionUpdate(screenTransition: ScreenTransition<*>?) {
        val transition = screenTransition?.transition

        when (transition) {
            is CommonScreens -> commonScreenTransition(screenTransition, transition)
            is ErrorScreenTransition -> errorsRouter.errorScreenTransition(screenTransition, transition)
        }
    }

    private fun commonScreenTransition(screenTransition: ScreenTransition<*>, transition: CommonScreens) {
        when (transition) {
            CommonScreens.SELECT_ADDRESS_ON_MAP -> {
                if (screenTransition.triggered)
                    return

                try {
                    val lat = screenTransition.bundle?.get("lat") as? Double
                    val lng = screenTransition.bundle?.get("lng") as? Double

                    if (lat != null && lng != null) {
                        screenTransition.bundle.clear()
                        val radius = 0.008

                        val intentBuilder: PlacePicker.IntentBuilder = PlacePicker.IntentBuilder()
                        val latlngBounds = LatLngBounds(
                            LatLng(lat - radius, lng - radius),
                            LatLng(lat + radius, lng + radius)
                        )

                        intentBuilder.setLatLngBounds(latlngBounds)
                        val intent = intentBuilder.build(context as Activity)

                        screenTransition.startActivityForResult(context, intent,
                            REQUEST_FOR_RESULT_SELECT_ADDRESS_ON_MAP)
                    }
                    else {
                        val intent = PlacePicker.IntentBuilder()
                            .build(context as Activity)

                        screenTransition.startActivityForResult(context, intent,
                            REQUEST_FOR_RESULT_SELECT_ADDRESS_ON_MAP)
                    }
                } catch (e: GooglePlayServicesNotAvailableException) {
                    context.longToast("GooglePlayServices недоступны").show()
                }
            }
        }
    }
}
