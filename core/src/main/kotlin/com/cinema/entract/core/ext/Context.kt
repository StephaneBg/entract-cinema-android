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

package com.cinema.entract.core.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import timber.log.Timber

inline fun <reified T : Activity> Context.start(bundle: Bundle? = null, configIntent: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(configIntent), bundle)
}

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

@ColorInt
fun Context.colorAttr(@AttrRes attribute: Int): Int = theme.color(attribute)

fun Context.attr(@AttrRes attribute: Int): TypedValue = theme.attr(attribute)

fun Context.isDarkTheme(): Boolean =
    configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

inline val Context.configuration: Configuration
    get() = resources.configuration

@ColorInt
private fun Resources.Theme.color(@AttrRes attribute: Int): Int {
    val attr = attr(attribute)
    if (attr.type < TypedValue.TYPE_FIRST_COLOR_INT || attr.type > TypedValue.TYPE_LAST_COLOR_INT) {
        throw IllegalArgumentException("Attribute value type is not color: $attribute")
    }

    return attr.data
}

private fun Resources.Theme.attr(@AttrRes attribute: Int): TypedValue {
    val typedValue = TypedValue()
    if (!resolveAttribute(attribute, typedValue, true)) {
        throw IllegalArgumentException("Failed to resolve attribute: $attribute")
    }

    return typedValue
}

fun Context.browse(url: String): Boolean = try {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
    true
} catch (e: ActivityNotFoundException) {
    Timber.e(e)
    false
}
