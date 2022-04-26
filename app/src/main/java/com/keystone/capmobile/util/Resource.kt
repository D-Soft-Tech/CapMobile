package com.keystone.capmobile.util

data class Resource<out T> (
    val status: Status,
    val data: T?,
    val message: String
) {
    companion object {
        fun<T> success(data: T?): Resource<T> = Resource(
            Status.SUCCESS,
            data,
            "Success"
        )

        fun<T> error(data: T?): Resource<T> = Resource(
            Status.ERROR,
            data,
            "Error fetching"
        )

        fun<T> loading(data: T?): Resource<T> = Resource(
            Status.LOADING,
            data,
            "Loading..."
        )

        fun<T> timeOut(data: T?): Resource<T> = Resource(
            Status.TIMEOUT,
            data,
            "Timeout - Connection is taking too long"
        )
    }
}