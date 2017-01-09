package com.deerhunter.simplestnewsfeedreader.inject.components

import com.deerhunter.simplestnewsfeedreader.news.views.NewsActivity
import com.deerhunter.simplestnewsfeedreader.inject.modules.MainActivityModule
import com.deerhunter.simplestnewsfeedreader.inject.scopes.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(activity: NewsActivity)
}