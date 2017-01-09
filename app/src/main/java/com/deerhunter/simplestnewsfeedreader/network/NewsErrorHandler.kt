package com.deerhunter.simplestnewsfeedreader.network

import com.deerhunter.simplestnewsfeedreader.R
import retrofit2.adapter.rxjava.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NewsErrorHandler {
    fun getError(throwable: Throwable): NetworkError {
        return when (throwable) {
            is SocketException, is SocketTimeoutException, is UnknownHostException -> NetworkError(R.string.no_internet_connection)
            is HttpException -> NetworkError(R.string.server_error_occured, arrayOf(throwable.code()))
            else -> NetworkError(R.string.unknown_error_occured)
        }
    }
}
