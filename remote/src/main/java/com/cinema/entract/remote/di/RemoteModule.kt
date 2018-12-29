/*
 * Copyright 2018 Stéphane Baiget
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

package com.cinema.entract.remote.di

import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.remote.RemoteRepoImpl
import com.cinema.entract.remote.createService
import com.cinema.entract.remote.mapper.*
import org.koin.dsl.module

val remoteModule = module {

    single { createService(get()) }

    single { MovieMapper() }
    single { DayMapper(get()) }
    single { WeekMapper(get()) }
    single { DateRangeMapper() }
    single { EventMapper() }

    factory<RemoteRepo> { RemoteRepoImpl(get(), get(), get(), get(), get()) }
}