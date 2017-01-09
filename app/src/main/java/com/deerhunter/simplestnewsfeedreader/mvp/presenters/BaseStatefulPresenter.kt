package com.deerhunter.simplestnewsfeedreader.mvp.presenters

import com.deerhunter.simplestnewsfeedreader.mvp.state.BaseState
import com.deerhunter.simplestnewsfeedreader.mvp.views.BaseView

interface BaseStatefulPresenter<in View : BaseView, State : BaseState> : BasePresenter<View> {
    fun subscribe(view: View, state: State?)
    fun getState(): State
}