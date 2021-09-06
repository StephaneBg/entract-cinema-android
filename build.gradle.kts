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

buildscript {
    repositories {
        maven("https://artifactory.f.bbg/artifactory/g-android-maven-proxy/")
        maven("https://artifactory.f.bbg/artifactory/maven-third-party-android-libs/")
    }

    dependencies {
        classpath(kotlin("gradle-plugin", Versions.kotlin))
        classpath(Build.androidGradle)
        classpath(Build.googleServices)
    }
}

allprojects {
    repositories {
        maven("https://artifactory.f.bbg/artifactory/g-android-maven-proxy/")
        maven("https://artifactory.f.bbg/artifactory/maven-third-party-android-libs/")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = Versions.java.toString()
        }
    }
}

subprojects {
    afterEvaluate {
        extensions.configure<com.android.build.gradle.BaseExtension> {
            compileOptions {
                sourceCompatibility = Versions.java
                targetCompatibility = Versions.java
            }
        }
    }
}
