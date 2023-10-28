package com.example.ktorchat.util

sealed class Resource<T>(val date: T? = null, val message: String? = null) {
    class Success<T>(date: T?) : Resource<T>(date)
    class Error<T>(message: String?) : Resource<T>(message = message)
}