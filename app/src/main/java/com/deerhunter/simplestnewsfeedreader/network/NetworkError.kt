package com.deerhunter.simplestnewsfeedreader.network

import android.support.annotation.StringRes

data class NetworkError(@StringRes val errorStringResId: Int, val params: Array<Any> = emptyArray())