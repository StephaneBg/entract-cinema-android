package com.cinema.entract.data.ext

import java.time.ZonedDateTime

fun ZonedDateTime.toEpochMilliSecond(): Long = this.toEpochSecond() * 1000
