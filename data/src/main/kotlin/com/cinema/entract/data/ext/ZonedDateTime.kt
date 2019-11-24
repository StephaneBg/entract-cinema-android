package com.cinema.entract.data.ext

import org.threeten.bp.ZonedDateTime

fun ZonedDateTime.toEpochMilliSecond(): Long = this.toEpochSecond() * 1000
