package com.deerhunter.simplestnewsfeedreader.news.state

import com.deerhunter.simplestnewsfeedreader.news.contracts.NewsContract
import com.deerhunter.simplestnewsfeedreader.news.data.NewsItem
import java.util.*

class NewsState(private val news: ArrayList<NewsItem>, private val isSavedNewsMode: Boolean) : NewsContract.State {
    override fun getLastNews(): ArrayList<NewsItem> = news
    override fun isSavedNewsMode() = isSavedNewsMode
}