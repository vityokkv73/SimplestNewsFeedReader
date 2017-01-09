package com.deerhunter.simplestnewsfeedreader.inject.modules

import android.app.Application
import com.deerhunter.simplestnewsfeedreader.Constants
import com.deerhunter.simplestnewsfeedreader.network.NewsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(val application: Application) {
    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_SERVER_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
    }

    @Provides @Singleton fun provideNewsService(retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }
}