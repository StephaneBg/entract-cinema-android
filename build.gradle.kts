import org.jlleitschuh.gradle.ktlint.KtlintExtension

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

buildscript {
    repositories {
        jcenter()
        google()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Build.androidGradle)
        classpath(Build.kotlinGradlePlugin)
        classpath(Build.googleServices)
        classpath(Build.ktlintGradle)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        maven("https://jitpack.io")
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        android.set(true)
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
