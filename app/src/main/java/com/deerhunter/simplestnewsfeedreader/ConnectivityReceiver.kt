package com.deerhunter.simplestnewsfeedreader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("connectivity Broadcast receiver")
        saveHasConnectionStateInPrefs(context, checkInternetConnection(context))
    }

    private fun saveHasConnectionStateInPrefs(context: Context, hasInternetConnection: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putBoolean(HAS_INTERNET_CONNECTION_PREF_KEY, hasInternetConnection).apply()
    }

    companion object {
        val HAS_INTERNET_CONNECTION_PREF_KEY = "has_internet_connection"

        fun getConnectivityObservable(context: Context): Observable<Boolean> {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return getSharedPrefUpdateObservable(prefs, HAS_INTERNET_CONNECTION_PREF_KEY)
                    .flatMap { key: String -> Observable.just(prefs.getBoolean(HAS_INTERNET_CONNECTION_PREF_KEY, true)) }
                    .startWith(Observable.just(checkInternetConnection(context)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

        private fun checkInternetConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
        }
    }
}
