package ru.kabylin.andrey.parking.ui.common

import android.support.annotation.StringRes
import junit.framework.Assert
import ru.kabylin.andrey.parking.*
import ru.kabylin.andrey.parking.client.AccessError
import ru.kabylin.andrey.parking.client.AccessErrorReason
import ru.kabylin.andrey.parking.ui.viewShouldBeVisible
import ru.kabylin.andrey.parking.views.ClientAppCompatActivity
import ru.kabylin.andrey.parking.views.ClientViewState

class DefaultErrorsResponder : ErrorResponder {
    override fun onCriticalError(activity: ClientAppCompatActivity<ClientViewState>) {
        // TODO
    }

    override fun onVersionError(activity: ClientAppCompatActivity<ClientViewState>) {
        // TODO
    }

    override fun onVersionWarning(activity: ClientAppCompatActivity<ClientViewState>) {
        // TODO
    }

    private fun checkBottomBoardText(activity: ClientAppCompatActivity<ClientViewState>, @StringRes except: Int) {
        Assert.assertEquals(
            activity.getText(except),
            activity.errorsView.bottomBoardErrorTitleTextView!!.text
        )
    }

    override fun onTooManyRequestError(activity: ClientAppCompatActivity<ClientViewState>) {
        activity.client.onError(AccessError(AccessErrorReason.TOO_MANY_REQUESTS))
        viewShouldBeVisible(activity.errorsView.bottomBoardErrorView!!)
        checkBottomBoardText(activity, R.string.too_many_requests)
    }

    override fun onTimeoutError(activity: ClientAppCompatActivity<ClientViewState>) {
        activity.client.onError(AccessError(AccessErrorReason.TIMEOUT))
        viewShouldBeVisible(activity.errorsView.bottomBoardErrorView!!)
        checkBottomBoardText(activity, R.string.time_is_out)
    }

    override fun onConnectionLostError(activity: ClientAppCompatActivity<ClientViewState>) {
        activity.client.onError(AccessError(AccessErrorReason.LOST_CONNECTION))
        viewShouldBeVisible(activity.errorsView.bottomBoardErrorView!!)
        checkBottomBoardText(activity, R.string.lost_connection)
    }

    override fun onNotFound(activity: ClientAppCompatActivity<ClientViewState>) {
        activity.client.onError(AccessError(AccessErrorReason.NOT_FOUND))
        viewShouldBeVisible(activity.errorsView.notFoundErrorView!!)
    }

    override fun onBadResponseError(activity: ClientAppCompatActivity<ClientViewState>) {
        activity.client.onError(AccessError(AccessErrorReason.BAD_RESPONSE))
        viewShouldBeVisible(activity.errorsView.bottomBoardErrorView!!)
        checkBottomBoardText(activity, R.string.bad_response)
    }

    override fun onInternalServerError(activity: ClientAppCompatActivity<ClientViewState>) {
        activity.client.onError(AccessError(AccessErrorReason.INTERNAL_SERVER_ERROR))
        viewShouldBeVisible(activity.errorsView.bottomBoardErrorView!!)
        checkBottomBoardText(activity, R.string.internal_server_error)
    }
}
