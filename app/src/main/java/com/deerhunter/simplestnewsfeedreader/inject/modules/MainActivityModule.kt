package com.deerhunter.simplestnewsfeedreader.inject.modules

import com.deerhunter.simplestnewsfeedreader.inject.scopes.ActivityScope
import com.deerhunter.simplestnewsfeedreader.news.presenters.NewsPresenter
import com.deerhunter.simplestnewsfeedreader.network.NewsService
import com.deerhunter.simplestnewsfeedreader.news.models.NewsModel
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    @Provides @ActivityScope fun provideNewsModel(apiService: NewsService): NewsModel {
        return NewsModel(apiService)
    }

    @Provides @ActivityScope fun provideNewsPresenter(newsModel: NewsModel): NewsPresenter {
        return NewsPresenter(newsModel)
    }
}