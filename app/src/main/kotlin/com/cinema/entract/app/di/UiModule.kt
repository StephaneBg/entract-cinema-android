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

package com.cinema.entract.app.di

import com.cinema.entract.app.mapper.DateRangeMapper
import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.NavigationViewModel
import com.cinema.entract.app.ui.TagViewModel
import com.cinema.entract.app.ui.settings.SettingsViewModel
import com.cinema.entract.app.ui.startup.StartupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    single { MovieMapper() }
    single { ScheduleMapper(get()) }
    single { DateRangeMapper() }

    viewModel { StartupViewModel(get()) }
    viewModel { CinemaViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { TagViewModel(get()) }
    viewModel { NavigationViewModel() }
}