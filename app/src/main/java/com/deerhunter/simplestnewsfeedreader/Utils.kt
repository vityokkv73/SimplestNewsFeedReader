package com.deerhunter.simplestnewsfeedreader

import android.content.Context
import android.content.SharedPreferences
import rx.Observable
import rx.subscriptions.Subscriptions

fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun getSharedPrefUpdateObservable(prefs: SharedPreferences, prefKey: String): Observable<String> {
    return fromSharedPreferencesChanges(prefs).filter { it == prefKey }
}

fun fromSharedPreferencesChanges(sharedPreferences: SharedPreferences): Observable<String> {
    return Observable.create<String> { subscriber ->
        val listener = { sp: SharedPreferences, key: String -> subscriber.onNext(key) }

        subscriber.add(Subscriptions.create { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) })

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
}