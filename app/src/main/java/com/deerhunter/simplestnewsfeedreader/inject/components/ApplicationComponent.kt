package com.deerhunter.simplestnewsfeedreader.inject.components

import com.deerhunter.simplestnewsfeedreader.NewsApplication
import com.deerhunter.simplestnewsfeedreader.inject.modules.ApplicationModule
import com.deerhunter.simplestnewsfeedreader.network.NewsService
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: NewsApplication)

    fun retrofit(): Retrofit
    fun newsService(): NewsService
}