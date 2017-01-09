package com.deerhunter.simplestnewsfeedreader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.deerhunter.simplestnewsfeedreader.inject.components.DaggerMainActivityComponent
import com.deerhunter.simplestnewsfeedreader.inject.components.MainActivityComponent
import com.deerhunter.simplestnewsfeedreader.inject.modules.MainActivityModule

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectMembers(DaggerMainActivityComponent.builder().
                applicationComponent((application as NewsApplication).component)
                .mainActivityModule(MainActivityModule())
                .build())
    }

    protected abstract fun injectMembers(component: MainActivityComponent)
}