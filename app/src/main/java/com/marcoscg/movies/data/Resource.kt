package com.marcoscg.movies.data

class Resource<T> private constructor(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS, ERROR, LOADING, EMPTY
    }
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }
        fun <T> error(message: String): Resource<T> {
            return Resource(
                Status.ERROR,
                null,
                message
            )
        }
        fun <T> loading(): Resource<T> {
            return Resource(
                Status.LOADING,
                null,
                null
            )
        }
        fun <T> empty(): Resource<T> {
            return Resource(
                Status.EMPTY,
                null,
                null
            )
        }
    }
}