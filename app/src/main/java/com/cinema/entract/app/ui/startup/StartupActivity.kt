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

package com.cinema.entract.app.ui.startup

import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.widget.ContentLoadingProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.cinema.CinemaActivity
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

class StartupActivity : BaseActivity() {

    private val startupViewModel by inject<StartupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_startup)
        find<ContentLoadingProgressBar>(R.id.progress).show()

        observe(startupViewModel.getEventUrl(), ::handleEvent)
    }

    private fun handleEvent(url: String?) {
        if (url.isNullOrEmpty()) {
            startActivity<CinemaActivity>()
        } else {
            Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .addListener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startActivity<CinemaActivity>()
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startActivity(intentFor<CinemaActivity>(CinemaActivity.EXTRA_EVENT_URL to url))
                        return true
                    }
                })
                .submit()
        }
    }
}