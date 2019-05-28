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

package com.cinema.entract.app.ui.schedule

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R

class ScheduleDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val marginMedium = context.resources.getDimension(R.dimen.margin_padding_size_medium).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (0 == position) {
            outRect.setEmpty()
        } else {
            when (parent.adapter?.getItemViewType(position)) {
                R.layout.list_item_schedule_week_header -> outRect.top = marginMedium
                R.layout.list_item_schedule_day_header -> outRect.top = marginMedium / 2
                R.layout.list_item_schedule_movie -> outRect.setEmpty()
            }
        }
    }
}