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
import org.threeten.bp.LocalDate
import timber.log.Timber

class ScheduleViewModel(
    private val useCase: CinemaUseCase,
    private val scheduleMapper: ScheduleMapper
) : BaseViewModel() {

    private val scheduleLiveData = MutableLiveData<Resource<List<ScheduleEntry>>>()

    fun getSchedule(): LiveData<Resource<List<ScheduleEntry>>> {
        scheduleLiveData.value ?: retrieveSchedule()
        return scheduleLiveData
    }

    fun retrieveSchedule() {
        scheduleLiveData.postValue(Loading())
        launchAsync(
            {
                val schedule = useCase.getSchedule()
                scheduleLiveData.postValue(Success(scheduleMapper.mapToUi(schedule)))
            },
            {
                Timber.e(it)
                scheduleLiveData.postValue(Error(it))
            }
        )
    }

    fun selectDate(date: LocalDate) = useCase.selectDate(date)
}