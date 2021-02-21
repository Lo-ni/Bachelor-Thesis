package de.asta.hochschule.trier.verleih.app.util

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import java.util.*

class DateValidatorWeekdays internal constructor() : DateValidator {
	
	private val utc = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE))
	
	override fun isValid(date: Long): Boolean {
		utc.timeInMillis = date
		val dayOfWeek: Int = utc.get(Calendar.DAY_OF_WEEK)
		return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
	}
	
	override fun describeContents(): Int {
		return 0
	}
	
	override fun writeToParcel(dest: Parcel, flags: Int) {}
	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}
		return other is DateValidatorWeekdays
	}
	
	override fun hashCode(): Int {
		val hashedFields = arrayOf<Any>()
		return hashedFields.contentHashCode()
	}
	
	companion object {
		// required for DateValidator
		val CREATOR: Creator<DateValidatorWeekdays?> = object : Creator<DateValidatorWeekdays?> {
			override fun createFromParcel(source: Parcel): DateValidatorWeekdays {
				return DateValidatorWeekdays()
			}
			
			override fun newArray(size: Int): Array<DateValidatorWeekdays?> {
				return arrayOfNulls(size)
			}
		}
		private const val TIMEZONE = "UTC"
	}
}