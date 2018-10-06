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

package com.cinema.entract.remote

import com.cinema.entract.core.utils.NetworkUtils
import com.cinema.entract.remote.network.ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun createService(networkUtils: NetworkUtils): CinemaService = Retrofit.Builder()
    .baseUrl("http://mobile-grenadecinema.fr/php/rest/")
    .client(makeOkHttpClient(networkUtils))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(CinemaService::class.java)

private fun makeOkHttpClient(networkUtils: NetworkUtils): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(ConnectivityInterceptor(networkUtils))
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}
