package com.cinema.entract.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cinema.entract.core.R
import com.cinema.entract.core.network.NoConnectivityException
import java.net.SocketTimeoutException

@StringRes
fun getErrorMessage(throwable: Throwable?): Int = when (throwable) {
    is NoConnectivityException -> R.string.error_no_connectivity
    is SocketTimeoutException -> R.string.error_server
    else -> R.string.error_general
}

@DrawableRes
fun getErrorDrawable(throwable: Throwable?): Int = when (throwable) {
    is NoConnectivityException -> R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp
    is SocketTimeoutException -> R.drawable.ic_error_outline_black_24dp
    else -> R.drawable.ic_error_outline_black_24dp
}
