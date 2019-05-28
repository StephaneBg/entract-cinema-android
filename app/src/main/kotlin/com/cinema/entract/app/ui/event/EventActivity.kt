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

package com.cinema.entract.app.ui.event

import android.os.Bundle
import android.widget.ImageView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.ui.TagAction
import com.cinema.entract.app.ui.TagViewModel
import com.cinema.entract.core.ui.BaseActivity
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventActivity : BaseActivity() {

    private val tagViewModel by viewModel<TagViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        find<ImageView>(R.id.cover).load(intent.getStringExtra(COVER_URL))
        find<MaterialButton>(R.id.close).setOnClickListener { finish() }

        tagViewModel.tag(TagAction.Event)
    }

    companion object {
        const val COVER_URL = "COVER_URL"
    }
}