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

package com.cinema.entract.app.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.show
import org.jetbrains.anko.find

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val message: TextView
    private val action: Button

    init {
        orientation = LinearLayout.VERTICAL
        id = R.id.errorView
        inflate(context, R.layout.widget_error_view, this)

        message = find(R.id.errorMessage)
        action = find(R.id.errorAction)
    }

    fun show(errorMessage: String, retryAction: () -> Unit) {
        message.text = errorMessage
        action.setOnClickListener { retryAction() }
        show()
    }
}