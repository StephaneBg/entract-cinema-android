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
    const val androidGradle = "7.0.2"
    const val googleServices = "4.3.2"
    const val kotlin = "1.5.30"
    const val coroutines = "1.5.2"
    const val appCompat = "1.3.1"
    const val recyclerView = "1.2.0"
    const val constraintLayout = "2.1.0"
    const val lifecyleViewmodel = "2.3.1"
    const val coreKtx = "1.6.0"
    const val fragmentKtx = "1.3.6"
    const val navigation = "2.3.5"
    const val material = "1.4.0"
    const val timber = "5.0.1"
    const val retrofit = "2.9.0"
    const val koin = "3.1.2"
    const val uniflow = "1.0.10"
    const val glide = "4.12.0"
    const val firebaseCore = "19.0.1"
    const val firebaseMessaging = "22.0.0"
}

object Build {
    val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
}

object Android {
    const val minSdkVersion = 21
    const val targetSdkVersion = 30
    const val compileSdkVersion = 30
}

object Libs {
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val lifecyleViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecyleViewmodel}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    val navFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    val desugaring = "com.android.tools:desugar_jdk_libs:1.1.5"

    val material = "com.google.android.material:material:${Versions.material}"
    val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
    val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"

    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    val uniflow = "org.uniflow-kt:uniflow-android:${Versions.uniflow}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
}
