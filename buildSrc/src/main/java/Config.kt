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

import org.gradle.api.JavaVersion

object Versions {
    val java = JavaVersion.VERSION_1_8
    const val androidGradle = "3.6.3"
    const val googleServices = "4.3.3"
    const val kotlin = "1.3.72"
    const val coroutines = "1.3.2"
    const val appCompat = "1.1.0"
    const val recyclerView = "1.1.0"
    const val material = "1.1.0"
    const val constraintLayout = "1.1.3"
    const val lifecyleViewmodel = "2.2.0"
    const val coreKtx = "1.2.0"
    const val fragmentKtx = "1.2.4"
    const val navigation = "2.2.2"
    const val timber = "4.7.1"
    const val retrofit = "2.8.1"
    const val anko = "0.10.8"
    const val koin = "2.1.5"
    const val uniflow = "0.10.2"
    const val glide = "4.11.0"
    const val jsr310 = "1.2.3"
    const val firebaseCore = "17.4.0"
    const val firebaseMessaging = "20.1.6"
}

object Build {
    val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
}

object Android {
    const val minSdkVersion = 21
    const val targetSdkVersion = 29
    const val compileSdkVersion = 29
}

object Libs {
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val anko = "org.jetbrains.anko:anko-commons:${Versions.anko}"

    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val lifecyleViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecyleViewmodel}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    val navFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    val material = "com.google.android.material:material:${Versions.material}"
    val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
    val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"

    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    val koinAndroid = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val uniflow = "io.uniflow:uniflow-androidx:${Versions.uniflow}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val jsr310 = "com.jakewharton.threetenabp:threetenabp:${Versions.jsr310}"
}
