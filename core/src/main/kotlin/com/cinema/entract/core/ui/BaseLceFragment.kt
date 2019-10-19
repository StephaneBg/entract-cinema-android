/*
 * Copyright 2019 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinema.entract.core.ui

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.cinema.entract.core.widget.ErrorView

open class BaseLceFragment : BaseFragment() {

    private lateinit var viewHolder: ViewHolder

    fun initLce(loading: ProgressBar, content: View, error: ErrorView) {
        viewHolder = ViewHolder(loading, content, error)
    }

    @CallSuper
    protected open fun showContent() {
        viewHolder.errorView.isVisible = false
        viewHolder.loadingView.isVisible = false
        viewHolder.contentView.isVisible = true
    }

    @CallSuper
    protected open fun showLoading() {
        viewHolder.contentView.isVisible = false
        viewHolder.errorView.isVisible = false
        viewHolder.loadingView.isVisible = true
    }

    @CallSuper
    protected open fun showError(throwable: Throwable?, action: () -> Unit) {
        viewHolder.contentView.isVisible = false
        viewHolder.loadingView.isVisible = false
        viewHolder.errorView.show(
            getErrorDrawable(throwable),
            getErrorMessage(throwable),
            action
        )
    }

    data class ViewHolder(
        val loadingView: ProgressBar,
        val contentView: View,
        val errorView: ErrorView
    )
}
