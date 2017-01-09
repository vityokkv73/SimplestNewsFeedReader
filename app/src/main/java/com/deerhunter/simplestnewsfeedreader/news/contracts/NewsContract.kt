package com.deerhunter.simplestnewsfeedreader.news.contracts

import android.support.annotation.StringRes
import com.deerhunter.simplestnewsfeedreader.mvp.models.BaseModel
import com.deerhunter.simplestnewsfeedreader.mvp.presenters.BaseStatefulPresenter
import com.deerhunter.simplestnewsfeedreader.mvp.state.BaseState
import com.deerhunter.simplestnewsfeedreader.mvp.views.BaseView
import com.deerhunter.simplestnewsfeedreader.mvp.views.ErrorableView
import com.deerhunter.simplestnewsfeedreader.mvp.views.ProgressView
import com.deerhunter.simplestnewsfeedreader.news.data.NewsItem
import rx.Observable
import java.util.*

interface NewsContract {
    interface View : BaseView, ProgressView, ErrorableView {
        fun showNews(news: List<NewsItem>)
        fun showMessage(@StringRes messageResId: Int)
        fun hideNoInternetConnectionView()
        fun showNoInternetConnectionView()
        fun showSaveNewsItemDialog(action: () -> Unit)
        fun showDeleteNewsItemDialog(action: () -> Unit)
    }

    interface Model : BaseModel {
        fun loadNews(): Observable<List<NewsItem>>
        fun getSavedNews(): Observable<List<NewsItem>>
        fun saveNewsItem(newsItem: NewsItem): Observable<Long>
        fun deleteNewsItem(newsItem: NewsItem): Observable<Boolean>
    }

    interface Presenter : BaseStatefulPresenter<View, State> {
        fun update()
        fun loadNews()
        fun loadSavedNews()
        fun saveNewsItem(newsItem: NewsItem)
        fun deleteNewsItem(newsItem: NewsItem)
        fun onNewsItemLongClick(newsItem: NewsItem)
        fun changeMode()
    }

    interface State : BaseState {
        fun getLastNews(): ArrayList<NewsItem>
        fun isSavedNewsMode(): Boolean
    }
}