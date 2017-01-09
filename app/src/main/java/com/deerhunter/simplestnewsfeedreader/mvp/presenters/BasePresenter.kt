package com.deerhunter.simplestnewsfeedreader.mvp.presenters

import com.deerhunter.simplestnewsfeedreader.mvp.views.BaseView

interface BasePresenter<in View : BaseView> {
    fun subscribe(view: View)
    fun unsubscribe()
}