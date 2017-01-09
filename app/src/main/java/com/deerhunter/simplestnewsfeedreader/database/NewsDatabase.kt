package com.deerhunter.simplestnewsfeedreader.database

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = NewsDatabase.NAME, version = NewsDatabase.VERSION, generatedClassSeparator = "_")
object NewsDatabase {
    const val NAME: String = "NewsDatabase"
    const val VERSION: Int = 1
}