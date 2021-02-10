package de.asta.hochschule.trier.verleih.helper

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

object DateHelper {
	
	const val DATE_FORMAT = "dd.MM.yyyy"
	const val TIME_FORMAT = "HH:mm"
	const val DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
	const val PRETTY_DATE_FORMAT = "dd.MMMM yyyy"
	const val TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm"
	
	fun getDateTime(dateString: String): DateTime {
		val formatter = DateTimeFormat.forPattern(TIMESTAMP_FORMAT).withLocale(Locale.getDefault())
		return formatter.parseDateTime(dateString)
	}
	
	fun getTimeString(dateTime: DateTime): String {
		return if (dateTime.minuteOfHour().get() < 10) {
			"${dateTime.hourOfDay().asString}:0${dateTime.minuteOfHour().asText}"
		} else {
			"${dateTime.hourOfDay().asString}:${dateTime.minuteOfHour().asText}"
		}
	}
}