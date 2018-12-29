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

package com.cinema.entract.cache.di

import com.cinema.entract.cache.mapper.DateRangeMapper
import com.cinema.entract.cache.mapper.DayMapper
import com.cinema.entract.cache.mapper.MovieMapper
import com.cinema.entract.cache.mapper.WeekMapper
import com.cinema.entract.cache.repository.CacheRepoImpl
import com.cinema.entract.cache.repository.UserPreferencesRepoImpl
import com.cinema.entract.data.repository.CacheRepo
import com.cinema.entract.data.repository.UserPreferencesRepo
import org.koin.dsl.module

val cacheModule = module {

    single { MovieMapper() }
    single { WeekMapper(get()) }
    single { DayMapper(get()) }
    single { DateRangeMapper() }
    single<CacheRepo> { CacheRepoImpl(get(), get(), get()) }
    single<UserPreferencesRepo> { UserPreferencesRepoImpl(get()) }
}