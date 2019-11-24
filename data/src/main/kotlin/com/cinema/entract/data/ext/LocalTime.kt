package com.cinema.entract.data.ext

import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("HH:mm")

fun LocalTime.formatToUi(): String = format(formatter)
