package io.github.kingg22.vibrion.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun LocalDateTime.toReadableString(): String = this.format(LocalDateTimeReadableFormat)

fun LocalDate.toReadableString(): String = this.format(LocalDateReadableFormat)

private val DayOfWeekNames.Companion.SPANISH_FULL_NAMES
    get() = DayOfWeekNames(
        monday = "lunes",
        tuesday = "martes",
        wednesday = "miércoles",
        thursday = "jueves",
        friday = "viernes",
        saturday = "sábado",
        sunday = "domingo",
    )

private val MonthNames.Companion.SPANISH_FULL_NAMES
    get() = MonthNames(
        january = "enero",
        february = "febrero",
        march = "marzo",
        april = "abril",
        may = "mayo",
        june = "junio",
        july = "julio",
        august = "agosto",
        september = "septiembre",
        october = "octubre",
        november = "noviembre",
        december = "diciembre",
    )

val LocalDateTimeReadableFormat = LocalDateTime.Format {
    dayOfWeek(DayOfWeekNames.SPANISH_FULL_NAMES)
    char(' ')
    day()
    char(' ')
    monthName(MonthNames.SPANISH_FULL_NAMES)
    char(' ')
    year()
    chars(", ")
    hour()
    char(':')
    minute()
    char(' ')
    amPmMarker(am = "AM", pm = "PM")
}

private val LocalDateReadableFormat = LocalDate.Format {
    dayOfWeek(DayOfWeekNames.SPANISH_FULL_NAMES)
    char(' ')
    day()
    char(' ')
    monthName(MonthNames.SPANISH_FULL_NAMES)
    char(' ')
    year()
}
