package com.deerhunter.simplestnewsfeedreader.news.models

import com.deerhunter.simplestnewsfeedreader.network.NewsService
import com.deerhunter.simplestnewsfeedreader.news.contracts.NewsContract
import com.deerhunter.simplestnewsfeedreader.news.data.NewsItem
import com.raizlabs.android.dbflow.sql.language.Select
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class NewsModel @Inject constructor(val apiService: NewsService) : NewsContract.Model {
    override fun loadNews(): Observable<List<NewsItem>> = apiService.loadNews().map { it.newsList }

    override fun getSavedNews(): Observable<List<NewsItem>> {
        return Observable.defer {
            Observable.just(Select()
                    .from(NewsItem::class.java)
                    .queryList())

        }.subscribeOn(Schedulers.io())
    }

    override fun saveNewsItem(newsItem: NewsItem): Observable<Long> {
        return Observable.defer {
            Observable.just(newsItem.insert())
                    .subscribeOn(Schedulers.io())
        }
    }

    override fun deleteNewsItem(newsItem: NewsItem): Observable<Boolean> {
        return Observable.defer {
            Observable.just(newsItem.delete())
                    .subscribeOn(Schedulers.io())
        }
    }
}