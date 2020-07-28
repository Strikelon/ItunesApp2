package com.example.itunesapp2.utils


data class ServerResponse<out T> (val status: ResponseStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): ServerResponse<T> =
            ServerResponse(status = ResponseStatus.SUCCESS, data = data, message = null)

        fun <T> error(message: String): ServerResponse<T> =
            ServerResponse(status = ResponseStatus.ERROR, data = null, message = message)

        fun <T> loading(): ServerResponse<T> =
            ServerResponse(status = ResponseStatus.LOADING, data = null, message = null)

        fun <T> cancelled() : ServerResponse<T> =
            ServerResponse(status = ResponseStatus.CANCELLED, data = null, message = null)
    }
}