package com.cinema.entract.core.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToTop() {
    smoothScrollToPosition(0)
}

fun RecyclerView.isScrolled(): Boolean =
    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() != 0
