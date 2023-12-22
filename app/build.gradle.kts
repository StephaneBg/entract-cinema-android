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
    id("kotlin-android")
    id("com.google.gms.google-services")
}

val versionMajor = 1
val versionMinor = 9
val versionPatch = 2

android {
    namespace = "com.cinema.entract.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        versionCode = versionMajor * 100 + versionMinor * 10 + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        resourceConfigurations.add("fr")
    }

    signingConfigs {
        create("release") {
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

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

    packaging {
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

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.coil)
    implementation(platform(libs.google.firebaseBom))
    implementation(libs.google.firebaseMessaging)

    coreLibraryDesugaring(libs.android.desugaring)
}
