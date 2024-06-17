package id.ac.istts.ecotong.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.ZoneId
import java.time.ZonedDateTime

fun getRelativeTime(isoString: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateTime =
        ZonedDateTime.parse(isoString, formatter.withZone(ZoneId.of("UTC"))).toLocalDateTime()

    val now = LocalDateTime.now(ZoneId.of("UTC"))

    val seconds = ChronoUnit.SECONDS.between(dateTime, now)
    val minutes = ChronoUnit.MINUTES.between(dateTime, now)
    val hours = ChronoUnit.HOURS.between(dateTime, now)
    val days = ChronoUnit.DAYS.between(dateTime, now)
    val weeks = ChronoUnit.WEEKS.between(dateTime, now)

    return when {
        seconds < 60 -> "$seconds seconds ago"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        else -> "$weeks weeks ago"
    }
}