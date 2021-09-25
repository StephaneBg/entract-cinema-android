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

plugins {
    id("com.android.application")
    kotlin("android")
}

val versionMajor = 1
val versionMinor = 7
val versionPatch = 0

android {
    compileSdk = Android.compileSdkVersion

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
        isCoreLibraryDesugaringEnabled = true
    }

    defaultConfig {
        applicationId = "com.cinema.entract.app"
        versionCode = versionMajor * 100 + versionMinor * 10 + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        vectorDrawables.useSupportLibrary = true
        minSdk = Android.minSdkVersion
        targetSdk = Android.targetSdkVersion
        resourceConfigurations.add("fr")
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            isJniDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    viewBinding { isEnabled = true }

    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    packagingOptions {
        resources {
            excludes.apply {
                add("**/*.kotlin_module")
                add("**/*.version")
                add("**/kotlin/**")
                add("**/*.txt")
                add("**/*.xml")
                add("**/*.properties")
            }
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":cache"))
    implementation(project(":remote"))
    implementation(project(":data"))

    implementation(kotlin("stdlib", Versions.kotlin))
    implementation(Libs.coroutinesCore)
    implementation(Libs.coroutinesAndroid)
    implementation(Libs.appCompat)
    implementation(Libs.coreKtx)
    implementation(Libs.recyclerView)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)
    implementation(Libs.lifecyleViewmodel)
    implementation(Libs.navFragment)
    implementation(Libs.navUi)
    implementation(Libs.koinAndroid)
    implementation(Libs.glide)
    implementation(Libs.firebaseCore)
    implementation(Libs.firebaseMessaging)
    implementation(Libs.timber)
    implementation(Libs.uniflow)

    coreLibraryDesugaring(Libs.desugaring)
}

plugins.apply("com.google.gms.google-services")
