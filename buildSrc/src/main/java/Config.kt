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

import org.gradle.api.JavaVersion

object Versions {
    val java = JavaVersion.VERSION_1_8
    val androidGradle = "3.2.0-rc03"
    val kotlin = "1.2.61"
    val coroutines = "0.24.0"
    val appcompat = "1.0.0-rc01"
    val recyclerView = "1.0.0-rc01"
    val material = "1.0.0-beta01"
    val constraintLayout = "1.1.0"
    val lifecyle = "2.0.0-beta01"
    val room = "2.0.0-alpha1"
    val timber = "4.5.1"
    val retrofit = "2.4.0"
    val retrofitCoroutines = "1.0.0"
    val anko = "0.10.5"
    val koin = "1.0.0-RC-2"
    val ktx = "1.0.0-alpha1"
    val glide = "4.8.0"
    val datePicker = "2.0.0"
}

object Build {
    val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Android {
    val buildToolsVersion = "28.0.2"
    val minSdkVersion = 21
    val targetSdkVersion = 28
    val compileSdkVersion = 28
    val applicationId = "com.cinema.entract"
    val versionCode = 1
    val versionName = "0.1"
}

object Libs {
    val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val anko = "org.jetbrains.anko:anko-commons:${Versions.anko}"

    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val vectorDrawable = "androidx.vectordrawable:vectordrawable:${Versions.appcompat}"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecyle}"
    val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecyle}"
    val room = "androidx.room:room-runtime:${Versions.room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"

    val material = "com.google.android.material:material:${Versions.material}"

    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    val retrofitAdapterCoroutines =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-experimental-adapter:${Versions.retrofitCoroutines}"
    val koinAndroid = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val datePicker = "com.github.prolificinteractive:material-calendarview:${Versions.datePicker}"
}
