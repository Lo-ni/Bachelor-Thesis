package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.*
import com.google.android.material.datepicker.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.*
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalDateTimeBinding
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel
import de.asta.hochschule.trier.verleih.util.DateValidatorWeekdays
import org.joda.time.*
import java.util.*
import kotlin.collections.ArrayList

class NewRentalDateTimeFragment : Fragment(R.layout.fragment_new_rental_date_time) {
	
	private lateinit var binding: FragmentNewRentalDateTimeBinding
	
	private val viewModel: NewRentalViewModel by activityViewModels()
	
	private var pickupDate: DateTime? = null
	private var pickupTime: DateTime? = null
	private var returnDate: DateTime? = null
	private var returnTime: DateTime? = null
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNewRentalDateTimeBinding.inflate(layoutInflater)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		setupDateTimeFields()
		
		binding.eventTitleEditText.doAfterTextChanged {
			viewModel.enterEventTitle(it.toString())
		}
		
		val datePicker = setupDatePicker()
		binding.pickupDateEditText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> datePicker.show(fm, TAG) }
		}
		binding.returnDateEditText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> datePicker.show(fm, TAG) }
		}
		
		val timePickerPickup = setupTimePicker(R.string.rental_pickup_time_select, true)
		binding.pickupTimeEditText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> timePickerPickup.show(fm, TAG) }
		}
		
		val timePickerReturn = setupTimePicker(R.string.rental_return_time_select, false)
		binding.returnTimeEditText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> timePickerReturn.show(fm, TAG) }
		}
	}
	
	private fun setupDateTimeFields() {
		pickupDate = viewModel.rentalLiveData.value?.pickupdate?.let { DateHelper.getDateTime(it) }
		pickupTime = viewModel.rentalLiveData.value?.pickupdate?.let { DateHelper.getDateTime(it) }
		returnDate = viewModel.rentalLiveData.value?.returndate?.let { DateHelper.getDateTime(it) }
		returnTime = viewModel.rentalLiveData.value?.returndate?.let { DateHelper.getDateTime(it) }
		
		setText(binding.eventTitleEditText, viewModel.rentalLiveData.value?.eventname)
		setText(binding.pickupDateEditText, pickupDate?.toString(DateHelper.DATE_FORMAT))
		setText(binding.returnDateEditText, returnDate?.toString(DateHelper.DATE_FORMAT))
		setText(binding.pickupTimeEditText, pickupTime?.toString(DateHelper.TIME_FORMAT))
		setText(binding.returnTimeEditText, returnTime?.toString(DateHelper.TIME_FORMAT))
	}
	
	private fun setText(view: TextInputEditText, text: String?) {
		if (text != null) {
			view.setText(text)
		}
	}
	
	private fun setupDatePicker(): MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {
		val todayMS = MaterialDatePicker.todayInUtcMilliseconds()
		val nextSixMonthCal = Calendar.getInstance(TIMEZONE_UTC)
		nextSixMonthCal.roll(Calendar.MONTH, 6)
		
		val validators = ArrayList<CalendarConstraints.DateValidator>()
		validators.add(DateValidatorWeekdays())
		validators.add(DateValidatorPointForward.now())
		
		val picker = MaterialDatePicker.Builder.dateRangePicker()
			.setTitleText(R.string.rental_date_range_select)
			.setCalendarConstraints(
				CalendarConstraints.Builder()
					.setStart(todayMS)
					.setEnd(nextSixMonthCal.timeInMillis)
					.setValidator(CompositeDateValidator.allOf(validators))
					.build()
			).build()
		picker.addOnPositiveButtonClickListener {
			val first = DateTime(it.first, DateTimeZone.UTC)
			val second = DateTime(it.second, DateTimeZone.UTC)
			updateDates(first, second)
		}
		return picker
	}
	
	private fun setupTimePicker(titleResId: Int, isPickupTime: Boolean): MaterialTimePicker {
		val picker = MaterialTimePicker.Builder()
			.setTimeFormat(TimeFormat.CLOCK_24H)
			.setHour(13)
			.setMinute(0)
			.setTitleText(titleResId)
			.build()
		picker.addOnPositiveButtonClickListener {
			val date = DateTime(1, 1, 1, picker.hour, picker.minute, DateTimeZone.UTC)
			if (isPickupTime) {
				updateTimes(date, null)
			} else {
				updateTimes(null, date)
			}
		}
		return picker
	}
	
	private fun updateDates(first: DateTime, second: DateTime) {
		pickupDate =
			DateTime(first.year, first.monthOfYear, first.dayOfMonth, 0, 0, DateTimeZone.UTC)
		binding.pickupDateEditText.setText(pickupDate?.toString(DateHelper.DATE_FORMAT))
		selectDate(pickupDate, pickupTime, true)
		
		returnDate =
			DateTime(second.year, second.monthOfYear, second.dayOfMonth, 0, 0, DateTimeZone.UTC)
		binding.returnDateEditText.setText(returnDate?.toString(DateHelper.DATE_FORMAT))
		selectDate(returnDate, returnTime, false)
	}
	
	private fun updateTimes(first: DateTime?, second: DateTime?) {
		if (first != null) {
			pickupTime = DateTime(1, 1, 1, first.hourOfDay, first.minuteOfHour, DateTimeZone.UTC)
			binding.pickupTimeEditText.setText(pickupTime?.toString(DateHelper.TIME_FORMAT))
			selectDate(pickupDate, pickupTime, true)
		}
		
		if (second != null) {
			returnTime = DateTime(1, 1, 1, second.hourOfDay, second.minuteOfHour, DateTimeZone.UTC)
			binding.returnTimeEditText.setText(returnTime?.toString(DateHelper.TIME_FORMAT))
			selectDate(returnDate, returnTime, false)
		}
	}
	
	private fun selectDate(date: DateTime?, time: DateTime?, isPickupDateTime: Boolean) {
		if (date != null && time != null) {
			val dateTime = DateTime(
				date.year,
				date.monthOfYear,
				date.dayOfMonth,
				time.hourOfDay,
				time.minuteOfHour,
				DateTimeZone.UTC
			)
			if (isPickupDateTime) {
				viewModel.selectPickupDate(dateTime)
			} else {
				viewModel.selectReturnDate(dateTime)
			}
		}
	}
	
	companion object {
		private const val TAG = "NewRentalDateTimeFragment"
		private val TIMEZONE_UTC = TimeZone.getTimeZone("UTC")
	}
}