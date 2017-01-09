package com.deerhunter.simplestnewsfeedreader.news.presenters

import android.content.Context
import com.deerhunter.simplestnewsfeedreader.ConnectivityReceiver
import com.deerhunter.simplestnewsfeedreader.R
import com.deerhunter.simplestnewsfeedreader.network.NewsErrorHandler
import com.deerhunter.simplestnewsfeedreader.news.contracts.NewsContract
import com.deerhunter.simplestnewsfeedreader.news.data.NewsItem
import com.deerhunter.simplestnewsfeedreader.news.state.NewsState
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

class NewsPresenter @Inject constructor(val model: NewsContract.Model) : NewsContract.Presenter {
    private var view: NewsContract.View? = null
    private var news: ArrayList<NewsItem>? = null
    private var isSavedNewsState = false
    private val viewSubscription = CompositeSubscription()

    override fun subscribe(view: NewsContract.View, state: NewsContract.State?) {
        this.view = view
        val lastNews = state?.getLastNews()
        news = lastNews
        isSavedNewsState = state?.isSavedNewsMode() ?: false
        if (lastNews?.isNotEmpty() == true) {
            view.showNews(ArrayList(lastNews))
        } else {
            if (!isSavedNewsState) {
                loadNews()
            } else {
                loadSavedNews()
            }
        }
    }

    override fun getState(): NewsContract.State {
        return NewsState(news ?: ArrayList(), isSavedNewsState)
    }

    override fun subscribe(view: NewsContract.View) {
        subscribe(view, null)
    }

    override fun unsubscribe() {
        view = null
        news = null
        viewSubscription.clear()
    }

    override fun update() {
        if (isSavedNewsState) {
            loadSavedNews()
        } else {
            loadNews()
        }
    }

    override fun loadNews() {
        isSavedNewsState = false
        view?.showProgress()
        viewSubscription.add(model.loadNews().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { data ->
                    news = ArrayList(data)
                    view?.hideProgress()
                    view?.showNews(data)
                },
                { throwable ->
                    view?.hideProgress()
                    view?.showNetworkError(NewsErrorHandler.getError(throwable))
                }
        ))
    }

    override fun loadSavedNews() {
        isSavedNewsState = true
        view?.showProgress()
        viewSubscription.add(model.getSavedNews().observeOn(AndroidSchedulers.mainThread()).subscribe(
                { data ->
                    news = ArrayList(data)
                    view?.hideProgress()
                    view?.showNews(data)
                },
                { throwable ->
                    view?.hideProgress()
                    view?.showError(R.string.problem_to_load_saved_news)
                }
        ))
    }

    override fun saveNewsItem(newsItem: NewsItem) {
        viewSubscription.add(model.saveNewsItem(newsItem).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { primaryKey ->
                    if (primaryKey != -1L) {
                        view?.showMessage(R.string.news_item_was_saved)
                    } else {
                        view?.showError(R.string.problem_to_save_news_item)
                    }
                },
                { throwable ->
                    // catch an exception instead of just checking if item exists - bad idea.
                    view?.showError(R.string.you_already_saved_this_news_item)
                }
        ))
    }

    override fun deleteNewsItem(newsItem: NewsItem) {
        viewSubscription.add(model.deleteNewsItem(newsItem).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { wasDeleted ->
                    if (wasDeleted) {
                        view?.showMessage(R.string.news_item_was_deleted)
                        news?.remove(newsItem)
                        // just to simplify code. Better to use RecyclerView.Adapter.notifyItemRemoved()
                        view?.showNews(news ?: ArrayList())
                    } else {
                        view?.showError(R.string.problem_to_delete_news_item)
                    }
                }
        ))
    }

    override fun changeMode() {
        news = ArrayList()
        view?.showNews(news!!)
        isSavedNewsState = !isSavedNewsState
        update()
    }

    override fun onNewsItemLongClick(newsItem: NewsItem) {
        if (isSavedNewsState) {
            view?.showDeleteNewsItemDialog { deleteNewsItem(newsItem) }
        } else {
            view?.showSaveNewsItemDialog { saveNewsItem(newsItem) }
        }
    }

    fun startConnectivityListening(context: Context) {
        viewSubscription.add(ConnectivityReceiver.getConnectivityObservable(context).subscribe(
                { hasInternetConnection ->
                    if (!hasInternetConnection) {
                        view?.showNoInternetConnectionView()
                    } else {
                        view?.hideNoInternetConnectionView()
                    }
                }
        ))
    }
}