package com.cinema.entract.data.ext

import java.time.Duration

fun Duration.formatToUi(): String = "${toHours()}h${toMinutesPart().toString().padStart(2, '0')}"

fun Duration.toMinutesPart(): Long = minusHours(toHours()).toMinutes()
