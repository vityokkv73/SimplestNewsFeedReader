package com.deerhunter.simplestnewsfeedreader.news.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.deerhunter.simplestnewsfeedreader.BaseActivity
import com.deerhunter.simplestnewsfeedreader.DetailsActivity
import com.deerhunter.simplestnewsfeedreader.news.NewsAdapter
import com.deerhunter.simplestnewsfeedreader.R
import com.deerhunter.simplestnewsfeedreader.inject.components.MainActivityComponent
import com.deerhunter.simplestnewsfeedreader.network.NetworkError
import com.deerhunter.simplestnewsfeedreader.news.contracts.NewsContract
import com.deerhunter.simplestnewsfeedreader.news.data.NewsItem
import com.deerhunter.simplestnewsfeedreader.news.presenters.NewsPresenter
import com.deerhunter.simplestnewsfeedreader.news.state.NewsState
import kotlinx.android.synthetic.main.news_activity.*
import timber.log.Timber
import javax.inject.Inject

class NewsActivity : BaseActivity(), NewsContract.View {
    @Inject lateinit var newsPresenter: NewsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.news_activity)
        setSupportActionBar(toolbar)

        swipeToRefresh.setOnRefreshListener { newsPresenter.update() }
        newsItemsList.adapter = NewsAdapter(this, { openDetailsActivity(it) }, { onNewsItemLongClick(it) })

        newsPresenter.subscribe(this, if (savedInstanceState != null) readState(savedInstanceState) else null)
        newsPresenter.startConnectivityListening(this)
    }

    override fun onDestroy() {
        newsPresenter.unsubscribe()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.news_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val actionMenu = menu.findItem(R.id.action)
        actionMenu.title = if (newsPresenter.getState().isSavedNewsMode())
            getString(R.string.load_news)
        else
            getString(R.string.show_saved_news)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action) {
            newsPresenter.changeMode()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openDetailsActivity(newsItem: NewsItem) {
        val intent = Intent(applicationContext, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.URL, newsItem.webURL)
        startActivity(intent)
    }

    private fun onNewsItemLongClick(newsItem: NewsItem) {
        newsPresenter.onNewsItemLongClick(newsItem)
    }

    override fun showNews(news: List<NewsItem>) {
        if (news.isEmpty()) {
            newsItemsList.visibility = View.INVISIBLE
        } else {
            newsItemsList.visibility = View.VISIBLE
            (newsItemsList.adapter as NewsAdapter).setItems(news)
        }
    }

    override fun hideNoInternetConnectionView() {
        noInternetConnection.visibility = View.GONE
    }

    override fun showNoInternetConnectionView() {
        noInternetConnection.visibility = View.VISIBLE
    }

    override fun showProgress() {
        swipeToRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        swipeToRefresh.isRefreshing = false
    }

    override fun showError(errorMsg: String) {
        Timber.e(errorMsg)
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun showError(errorResId: Int) {
        showError(getString(errorResId))
    }

    override fun showMessage(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_LONG).show()
    }

    override fun showNetworkError(networkError: NetworkError) {
        val errorMsg = getString(networkError.errorStringResId, networkError.params)
        Timber.e(errorMsg)
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun showSaveNewsItemDialog(action: () -> Unit) {
        AlertDialog.Builder(this).setMessage(R.string.do_you_wanna_save_this_news_item)
                .setNegativeButton(R.string.no, { dialog, i -> })
                .setPositiveButton(R.string.yes, { dialog, i -> action.invoke() })
                .show()
    }

    override fun showDeleteNewsItemDialog(action: () -> Unit) {
        AlertDialog.Builder(this).setMessage(R.string.do_you_wanna_delete_this_news_item)
                .setNegativeButton(R.string.no, { dialog, i -> })
                .setPositiveButton(R.string.yes, { dialog, i -> action.invoke() })
                .show()
    }

    override fun injectMembers(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveState(outState, newsPresenter.getState())
        super.onSaveInstanceState(outState)
    }

    companion object {
        private val NEWS_KEY = "NEWS"
        private val STATE_KEY = "STATE"
        fun saveState(bundle: Bundle, newsState: NewsContract.State) {
            bundle.putParcelableArrayList(NEWS_KEY, newsState.getLastNews())
            bundle.putBoolean(STATE_KEY, newsState.isSavedNewsMode())
        }

        fun readState(bundle: Bundle): NewsContract.State {
            return NewsState(bundle.getParcelableArrayList<NewsItem>(NEWS_KEY), bundle.getBoolean(STATE_KEY))
        }
    }
}
