package com.cinema.entract.data.ext

import java.time.Duration

fun Duration.formatToUi(): String = "${toHours()}h${toMinutesCustom().toString().padStart(2, '0')}"

fun Duration.toMinutesCustom(): Long = minusHours(toHours()).toMinutes()
