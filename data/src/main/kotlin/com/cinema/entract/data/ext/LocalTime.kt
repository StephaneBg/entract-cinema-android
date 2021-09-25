package com.cinema.entract.data.ext

import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("HH:mm")

fun LocalTime.formatToUi(): String = format(formatter)
