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

package com.cinema.entract.cache.repository

import android.content.Context
import androidx.core.content.edit
import com.cinema.entract.data.repository.UserPreferencesRepo

class UserPreferencesRepoImpl(context: Context) : UserPreferencesRepo {

    private val preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    override fun isEventEnabled(): Boolean = preferences.getBoolean(USER_PREF_EVENT, true)

    override fun setEventPreference(enabled: Boolean) = preferences.edit {
        putBoolean(USER_PREF_EVENT, enabled)
    }

    override fun isOnlyOnWifi(): Boolean = preferences.getBoolean(USER_PREF_DATA, false)

    override fun setOnlyOnWifi(onlyOnWifi: Boolean) = preferences.edit {
        putBoolean(USER_PREF_DATA, onlyOnWifi)
    }

    override fun getThemeMode(): Int = preferences.getInt(USER_PREF_THEME_MODE, -1)

    override fun setThemeMode(mode: Int) = preferences.edit {
        putInt(USER_PREF_THEME_MODE, mode)
    }

    companion object {
        private const val USER_PREFS = "USER_PREFS"
        private const val USER_PREF_EVENT = "USER_PREF_EVENT"
        private const val USER_PREF_DATA = "USER_PREF_DATA"
        private const val USER_PREF_THEME_MODE = "USER_PREF_THEME_MODE"
    }
}