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

package com.cinema.entract.app.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.ui.base.BaseViewModel
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.data.interactor.CinemaUseCase
import timber.log.Timber

class ScheduleViewModel(
    useCase: CinemaUseCase,
    private val scheduleMapper: ScheduleMapper
) : BaseViewModel<CinemaUseCase>(useCase) {

    private val schedule = MutableLiveData<Resource<List<ScheduleEntry>>>()

    fun getSchedule(): LiveData<Resource<List<ScheduleEntry>>> {
        schedule.value ?: retrieveSchedule()
        return schedule
    }

    fun retrieveSchedule() {
        schedule.postValue(Loading())
        launchAsyncTryCatch(
            {
                schedule.postValue(Success(scheduleMapper.mapToUi(useCase.getSchedule())))
            },
            {
                Timber.e(it)
                schedule.postValue(Error(it))
            }
        )
    }
}