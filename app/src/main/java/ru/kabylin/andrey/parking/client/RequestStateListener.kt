package ru.kabylin.andrey.parking.client

interface RequestStateListener {
    fun onRequestStateUpdated(requestState: ClientResponse<RequestState>)
}
