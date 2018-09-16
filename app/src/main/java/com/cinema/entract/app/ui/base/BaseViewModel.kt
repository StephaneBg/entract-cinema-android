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

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.cinema.entract.data.interactor.BaseUseCase
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


open class BaseViewModel<out UseCase : BaseUseCase>(val useCase: UseCase) : ViewModel() {

    private val asyncJobs = mutableListOf<Job>()

    fun <T> launchAsync(
        function: suspend CoroutineScope.() -> T,
        catch: (Throwable) -> T
    ) {
        val job = launch(UI) {
            try {
                function()
            } catch (e: Throwable) {
                if (e !is CancellationException) catch(e)
            }
        }
        job.invokeOnCompletion {
            asyncJobs.remove(job)
        }
        asyncJobs.add(job)
    }

    @CallSuper
    @Synchronized
    protected fun cancelAllAsync() {
        val asyncJobsSize = asyncJobs.size
        if (asyncJobsSize > 0) {
            for (i in asyncJobsSize - 1 downTo 0) {
                asyncJobs[i].cancel()
            }
        }
    }

    override fun onCleared() {
        cancelAllAsync()
    }
}