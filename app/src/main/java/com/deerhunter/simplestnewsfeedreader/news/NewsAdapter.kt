package com.deerhunter.simplestnewsfeedreader.news

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.deerhunter.simplestnewsfeedreader.R
import com.deerhunter.simplestnewsfeedreader.dpToPx
import com.deerhunter.simplestnewsfeedreader.news.data.NewsItem
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.newsitem_content.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(context: Context, val clickAction: (NewsItem) -> Unit, val longClickAction: (NewsItem) -> Unit) : RecyclerView.Adapter<NewsViewHolder>() {
    var newsItems: List<NewsItem> = ArrayList()
    private var lastPosition = -1
    private var animationDistance: Int = context.resources.getDimensionPixelSize(R.dimen.news_adapter_item_animation_shift)

    override fun getItemCount() = newsItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newsitem_content, parent, false)
        return NewsViewHolder(view, clickAction, longClickAction)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindNewsItem(newsItems[position])
        if (position > lastPosition) {
            lastPosition = position
            ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y, animationDistance.toFloat(), 0f).start()
        }
    }

    override fun onViewRecycled(holder: NewsViewHolder) {
        holder.unbindNewsItem()
    }

    fun setItems(items: List<NewsItem>) {
        newsItems = items
        notifyDataSetChanged()
    }
}

class NewsViewHolder(view: View, val clickAction: (NewsItem) -> Unit, val longClickAction: (NewsItem) -> Unit) : RecyclerView.ViewHolder(view) {
    fun bindNewsItem(newsItem: NewsItem) {
        with(newsItem) {
            Picasso.with(itemView.context)
                    .load(image.photo)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(dpToPx(itemView.context, 4f).toInt(), 0))
                    .into(itemView.image)

            val date = parseISTDate(dateLine)
            itemView.agencyAndTime.text = itemView.context.getString(R.string.agency_time_format, agency, DateUtils.getRelativeTimeSpanString(date.time))
            itemView.headLine.text = headline
            itemView.setOnClickListener { clickAction(this) }
            itemView.setOnLongClickListener {
                longClickAction(this)
                true
            }
        }
    }

    fun unbindNewsItem() {
        Picasso.with(itemView.context).cancelRequest(itemView.image)
    }

    private fun parseISTDate(dateString: String): Date {
        val format = SimpleDateFormat("MMM d, yyyy, hh.mma z", Locale("en", "IN"))
        return try {
            format.parse(dateString)
        } catch (ex: Exception) {
            Timber.d(ex)
            Calendar.getInstance().time
        }
    }
}
