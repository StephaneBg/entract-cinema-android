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

package com.cinema.entract.app.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cinema.entract.app.R
import com.cinema.entract.core.ext.colorAttr

fun ImageView.load(url: String?) {
    Glide.with(context).load(url).into(this)
}

fun ImageView.displayPlaceHolder() {
    with(this) {
        adjustViewBounds = true
        setImageResource(R.drawable.ic_movie_black_24dp)
        setColorFilter(context.colorAttr(R.attr.colorOnSurface))
        setBackgroundColor(context.colorAttr(R.attr.colorSurface))
    }
}
