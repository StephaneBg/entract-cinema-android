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

package com.cinema.entract.core.ext

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.cinema.entract.core.R

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

@SuppressLint("Recycle")
fun Context.styledColor(id: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

@SuppressLint("PrivateResource")
fun Context.primaryColor(): Int = styledColor(R.attr.colorPrimary)

fun Context.secondaryColor(): Int = styledColor(R.attr.colorSecondary)