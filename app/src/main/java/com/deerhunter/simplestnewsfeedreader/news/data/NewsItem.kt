package com.deerhunter.simplestnewsfeedreader.news.data

import android.os.Parcel
import android.os.Parcelable
import com.deerhunter.simplestnewsfeedreader.database.NewsDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.modelAdapter
import com.raizlabs.android.dbflow.structure.Model

@Table(database = NewsDatabase::class)
data class NewsItem(@SerializedName("Agency") @Column var agency: String,
                    @SerializedName("Caption") @Column var caption: String,
                    @SerializedName("CommentCountUrl") @Column var commentCountUrl: String,
                    @SerializedName("CommentFeedUrl") @Column var commentFeedUrl: String,
                    @SerializedName("DateLine") @Column var dateLine: String,
                    @SerializedName("HeadLine") @Column var headline: String,
                    @SerializedName("Image") @ForeignKey(saveForeignKeyModel = true) var image: Image,
                    @SerializedName("Keywords") @Column var keywords: String,
                    @SerializedName("NewsItemId") @PrimaryKey var newsItemId: String,
                    @SerializedName("Related") @Column var related: String,
                    @SerializedName("Story") @Column var story: String,
                    @SerializedName("WebURL") @Column var webURL: String
) : Parcelable, Model {
    constructor() : this("", "", "", "", "", "", Image(), "", "", "", "", "")

    override fun load() = modelAdapter<NewsItem>().load(this)

    override fun save() = modelAdapter<NewsItem>().save(this)

    override fun delete() = modelAdapter<NewsItem>().delete(this)

    override fun update() = modelAdapter<NewsItem>().update(this)

    override fun insert() = modelAdapter<NewsItem>().insert(this)

    override fun exists() = modelAdapter<NewsItem>().exists(this)

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<NewsItem> = object : Parcelable.Creator<NewsItem> {
            override fun createFromParcel(source: Parcel): NewsItem = NewsItem(source)
            override fun newArray(size: Int): Array<NewsItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readParcelable<Image>(Image::class.java.classLoader), source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(agency)
        dest?.writeString(caption)
        dest?.writeString(commentCountUrl)
        dest?.writeString(commentFeedUrl)
        dest?.writeString(dateLine)
        dest?.writeString(headline)
        dest?.writeParcelable(image, 0)
        dest?.writeString(keywords)
        dest?.writeString(newsItemId)
        dest?.writeString(related)
        dest?.writeString(story)
        dest?.writeString(webURL)
    }
}