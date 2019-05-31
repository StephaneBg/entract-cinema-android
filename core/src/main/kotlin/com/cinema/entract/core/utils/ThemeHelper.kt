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

package com.cinema.entract.core.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDelegate
import com.cinema.entract.core.R

fun convertThemeMode(@IdRes themeId: Int): Int = when (themeId) {
    R.id.theme_light -> AppCompatDelegate.MODE_NIGHT_NO
    R.id.theme_dark -> AppCompatDelegate.MODE_NIGHT_YES
    else -> if (isQOrAbove()) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
}
