/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.ui.view.base

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.cinema.entract.ui.R
import com.cinema.entract.ui.ext.find
import com.cinema.entract.ui.ext.hide
import com.cinema.entract.ui.ext.show

@Suppress("UNCHECKED_CAST")
open class BaseLceFragment<T : View> : BaseFragment() {

    lateinit var contentView: T
    lateinit var loadingView: View
    lateinit var errorView: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contentView = find<View>(R.id.contentView) as T
        loadingView = find(R.id.loadingView)
        errorView = find(R.id.errorView)
    }

    fun showContent() {
        errorView.hide()
        loadingView.hide()
        contentView.show()
    }

    fun showLoading() {
        contentView.hide()
        errorView.hide()
        loadingView.show()
    }

    fun showError(throwable: Throwable?, action: () -> Unit) {
        contentView.hide()
        loadingView.hide()
        errorView.text = getErrorMessage(throwable)
        errorView.setOnClickListener { action() }
        errorView.show()
    }
}