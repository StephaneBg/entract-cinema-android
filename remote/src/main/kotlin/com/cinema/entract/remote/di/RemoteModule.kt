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

package com.cinema.entract.remote.di

import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.remote.CinemaApi
import com.cinema.entract.remote.CinemaRemoteRepo
import com.cinema.entract.remote.createClient
import com.cinema.entract.remote.mapper.DateRangeMapper
import com.cinema.entract.remote.mapper.DayMapper
import com.cinema.entract.remote.mapper.MovieMapper
import com.cinema.entract.remote.mapper.PromotionalMapper
import com.cinema.entract.remote.mapper.WeekMapper
import org.koin.dsl.module

val remoteModule = module {

    single { createClient() }
    single { CinemaApi(get()) }

    single { MovieMapper() }
    single { DayMapper(get()) }
    single { WeekMapper(get()) }
    single { DateRangeMapper() }
    single { PromotionalMapper() }

    factory<RemoteRepo> { CinemaRemoteRepo(get(), get(), get(), get(), get()) }
}
