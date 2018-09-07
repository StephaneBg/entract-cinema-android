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

package com.cinema.entract.ui

import android.app.Application
import com.cinema.entract.cache.di.dataModule
import com.cinema.entract.cache.di.remoteModule
import com.cinema.entract.domain.di.domainModule
import com.cinema.entract.ui.di.uiModule
import org.koin.android.ext.android.startKoin
import org.koin.log.EmptyLogger
import timber.log.Timber


class CinemaEntractApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin(
            this,
            listOf(
                remoteModule,
                dataModule,
                domainModule,
                uiModule
            ),
            logger = EmptyLogger()
        )
    }
}