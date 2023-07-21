package com.ilhomsoliev.rikandmortytest.domain


sealed class Response<T : Any> {
    class Success<T: Any>(val data: T) : Response<T>()
    class Loading<T: Any>() : Response<T>()
    class Error<T: Any>(val message: String) : Response<T>()
}