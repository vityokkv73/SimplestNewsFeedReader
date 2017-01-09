package com.deerhunter.simplestnewsfeedreader.mvp.views

import android.support.annotation.StringRes
import com.deerhunter.simplestnewsfeedreader.network.NetworkError

interface ErrorableView {
    fun showError(errorMsg: String)
    fun showError(@StringRes errorResId: Int)
    fun showNetworkError(networkError: NetworkError)
}