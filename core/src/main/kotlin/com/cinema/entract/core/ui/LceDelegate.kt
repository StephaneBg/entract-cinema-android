package com.cinema.entract.core.ui

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.cinema.entract.core.widget.ErrorView

interface Lce {
    var viewHolder: ViewHolder

    fun initLce(loading: ProgressBar, content: View, error: ErrorView)
    fun showContent()
    fun showLoading()
    fun showError(throwable: Throwable?, action: () -> Unit)
}

class LceDelegate : Lce {

    override lateinit var viewHolder: ViewHolder

    override fun initLce(loading: ProgressBar, content: View, error: ErrorView) {
        viewHolder = ViewHolder(loading, content, error)
    }

    override fun showContent() {
        viewHolder.errorView.isVisible = false
        viewHolder.loadingView.isVisible = false
        viewHolder.contentView.isVisible = true
    }

    override fun showLoading() {
        viewHolder.contentView.isVisible = false
        viewHolder.errorView.isVisible = false
        viewHolder.loadingView.isVisible = true
    }

    override fun showError(throwable: Throwable?, action: () -> Unit) {
        viewHolder.contentView.isVisible = false
        viewHolder.loadingView.isVisible = false
        viewHolder.errorView.show(
            getErrorDrawable(throwable),
            getErrorMessage(throwable),
            action
        )
    }
}

data class ViewHolder(
    val loadingView: ProgressBar,
    val contentView: View,
    val errorView: ErrorView
)
