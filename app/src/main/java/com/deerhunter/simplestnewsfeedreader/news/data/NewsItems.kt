package com.deerhunter.simplestnewsfeedreader.news.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class NewsItems(@SerializedName("NewsItem") val newsList: ArrayList<NewsItem>)