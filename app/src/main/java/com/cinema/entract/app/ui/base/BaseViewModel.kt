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

package com.cinema.entract.app.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


open class BaseViewModel : ViewModel() {

    private val job = Job()

    fun <T> launchAsync(
        tryBlock: suspend CoroutineScope.() -> T,
        catchBlock: (Throwable) -> T
    ) {
        launch(UI, parent = job) {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (e !is CancellationException) catchBlock(e)
            }
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}