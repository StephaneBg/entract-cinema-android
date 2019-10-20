package com.cinema.entract.core.utils

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class EmptinessHelper {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View

    fun init(recyclerView: RecyclerView, emptyView: View) {
        try {
            this.recyclerView = recyclerView
            this.emptyView = emptyView
            this.recyclerView.adapter?.registerAdapterDataObserver(observer)
        } catch (exception: Exception) {
            Timber.e(exception)
        } finally {
            this.emptyView.isVisible = false
        }
    }

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    private fun checkIfEmpty() {
        val isEmpty = recyclerView.adapter?.itemCount == 0
        recyclerView.isVisible = !isEmpty
        emptyView.isVisible = isEmpty
    }
}
