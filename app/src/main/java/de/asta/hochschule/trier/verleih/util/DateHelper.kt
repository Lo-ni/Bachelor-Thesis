package de.asta.hochschule.trier.verleih.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
	
	const val DATE_FORMAT = "dd.MM.yyyy"
	const val TIME_FORMAT = "HH:mm"
	const val DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
	const val PRETTY_DATE_FORMAT = "dd.MMMM yyyy"
	const val TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm"
	const val LONG_DATE_TIME_FORMAT = "EEE, dd.MM.yyyy HH:mm"
	
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
	
	fun timeStampToString(timeStamp: Long, format: String): String {
		val date = Date(timeStamp)
		val formatter = SimpleDateFormat(format, Locale.getDefault())
		return formatter.format(date)
	}
}