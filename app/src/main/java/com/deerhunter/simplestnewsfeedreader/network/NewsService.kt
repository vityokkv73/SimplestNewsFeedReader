package com.deerhunter.simplestnewsfeedreader.network

import com.deerhunter.simplestnewsfeedreader.news.data.NewsItems
import retrofit2.http.GET
import rx.Observable

interface NewsService {
    @GET("/feeds/newsdefaultfeeds.cms?feedtype=sjson")
    fun loadNews(): Observable<NewsItems>
}