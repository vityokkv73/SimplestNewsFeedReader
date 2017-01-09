package com.deerhunter.simplestnewsfeedreader.news.data

import android.os.Parcel
import android.os.Parcelable
import com.deerhunter.simplestnewsfeedreader.database.NewsDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.modelAdapter
import com.raizlabs.android.dbflow.structure.Model

@Table(database = NewsDatabase::class)
data class Image(
        @SerializedName("Photo") @Column var photo: String,
        @SerializedName("Thumb") @Column var thumb: String,
        @SerializedName("PhotoCaption") @Column var photoCaption: String
) : Parcelable, Model {
    @PrimaryKey(autoincrement = true) var id: Long = 0

    constructor() : this("", "", "")

    override fun load() = modelAdapter<Image>().load(this)

    override fun save() = modelAdapter<Image>().save(this)

    override fun delete() = modelAdapter<Image>().delete(this)

    override fun update() = modelAdapter<Image>().update(this)

    override fun insert() = modelAdapter<Image>().insert(this)

    override fun exists() = modelAdapter<Image>().exists(this)

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Image> = object : Parcelable.Creator<Image> {
            override fun createFromParcel(source: Parcel): Image = Image(source)
            override fun newArray(size: Int): Array<Image?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(photo)
        dest?.writeString(thumb)
        dest?.writeString(photoCaption)
    }
}