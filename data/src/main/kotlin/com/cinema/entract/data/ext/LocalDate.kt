/*
 * Copyright 2018 Stéphane Baiget
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

package com.cinema.entract.data.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

private val longFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRANCE)
private val shortFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale.FRANCE)

fun LocalDate.longFormatToUi(): String = when {
    this.isToday() -> "Aujourd'hui"
    this.isTomorrow() -> "Demain"
    else -> this.format(longFormatter).capitalize()
}

fun LocalDate.shortFormatToUi(): String = this.format(shortFormatter).capitalize()

fun LocalDate.formatToUTC(): String = this.format(DateTimeFormatter.ISO_LOCAL_DATE)

fun LocalDate.isTodayOrLater(): Boolean = this.isAfter(LocalDate.now().minusDays(1))

fun LocalDate.isToday(): Boolean = this == LocalDate.now()

fun LocalDate.isTomorrow(): Boolean = this == LocalDate.now().plusDays(1)

infix fun LocalDate.sameMonth(other: LocalDate): Boolean = this.monthValue == other.monthValue

fun LocalDate.toEpochMilliSecond() = this.toEpochDay() * 24 * 60 * 60 * 1000