package com.deerhunter.simplestnewsfeedreader

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.deerhunter.simplestnewsfeedreader.inject.components.ApplicationComponent
import com.deerhunter.simplestnewsfeedreader.inject.components.DaggerApplicationComponent
import com.deerhunter.simplestnewsfeedreader.inject.modules.ApplicationModule
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

class NewsApplication : Application() {
    lateinit var component: ApplicationComponent
    override fun onCreate() {
        component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        component.inject(this)

        registerConnectivityReceiver()
        FlowManager.init(FlowConfig.Builder(this).openDatabasesOnInit(true).build())
    }

    private fun registerConnectivityReceiver() {
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}