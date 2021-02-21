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
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel
import de.asta.hochschule.trier.verleih.util.*
import org.joda.time.*
import java.util.*
import kotlin.collections.ArrayList

class NewRentalDateTimeFragment : Fragment(R.layout.fragment_new_rental_date_time) {
	
	private lateinit var binding: FragmentNewRentalDateTimeBinding
	private val viewModel: NewRentalViewModel by activityViewModels()
	
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
		
		binding.eventTitleEditText.doAfterTextChanged {
			binding.eventTitleEditText.error = null
			viewModel.enterEventTitle(it.toString())
		}
		
		setupDateTimeFields()
		setupDateTimeEditText(binding.pickupDateEditText, null, null)
		setupDateTimeEditText(binding.returnDateEditText, null, null)
		setupDateTimeEditText(binding.pickupTimeEditText, R.string.rental_pickup_time_select, true)
		setupDateTimeEditText(binding.returnTimeEditText, R.string.rental_return_time_select, false)
		
		viewModel.validPagesLiveData.observe(viewLifecycleOwner, {
			showEditTextError(binding.eventTitleEditText)
			showEditTextError(binding.pickupDateEditText)
			showEditTextError(binding.pickupTimeEditText)
			showEditTextError(binding.returnDateEditText)
			showEditTextError(binding.returnTimeEditText)
		})
	}
	
	private fun setupDateTimeEditText(
		editText: TextInputEditText,
		stringResId: Int?,
		isPickupTime: Boolean?
	) {
		val picker = if (stringResId != null && isPickupTime != null) {
			setupTimePicker(stringResId, isPickupTime)
		} else {
			setupDatePicker()
		}
		
		editText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> picker.show(fm, TAG) }
		}
		editText.doAfterTextChanged {
			editText.error = null
		}
	}
	
	private fun showEditTextError(editText: TextInputEditText) {
		editText.error = if (editText.text.isNullOrBlank()) {
			context?.getString(R.string.input_required)
		} else {
			null
		}
	}
	
	private fun setupDateTimeFields() {
		val pickupDate =
			viewModel.rentalLiveData.value?.pickupdate?.let { DateHelper.getDateTime(it) }
		val pickupTime =
			viewModel.rentalLiveData.value?.pickupdate?.let { DateHelper.getDateTime(it) }
		val returnDate =
			viewModel.rentalLiveData.value?.returndate?.let { DateHelper.getDateTime(it) }
		val returnTime =
			viewModel.rentalLiveData.value?.returndate?.let { DateHelper.getDateTime(it) }
		
		if (viewModel.rentalLiveData.value?.eventname != null) {
			binding.eventTitleEditText.setText(viewModel.rentalLiveData.value?.eventname)
		}
		binding.pickupDateEditText.setText(pickupDate?.toString(DateHelper.DATE_FORMAT))
		binding.returnDateEditText.setText(returnDate?.toString(DateHelper.DATE_FORMAT))
		binding.pickupTimeEditText.setText(pickupTime?.toString(DateHelper.TIME_FORMAT))
		binding.returnTimeEditText.setText(returnTime?.toString(DateHelper.TIME_FORMAT))
	}
	
	private fun setupDatePicker(): MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {
		val todayMS = MaterialDatePicker.todayInUtcMilliseconds()
		val nextSixMonthCal = Calendar.getInstance(TIMEZONE_UTC)
		nextSixMonthCal.roll(Calendar.MONTH, 6)
		val tomorrowCal = Calendar.getInstance(TIMEZONE_UTC)
		tomorrowCal.roll(Calendar.DAY_OF_MONTH, 1)
		
		val validators = ArrayList<CalendarConstraints.DateValidator>()
		validators.add(DateValidatorWeekdays())
		validators.add(DateValidatorPointForward.from(tomorrowCal.timeInMillis))
		
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
			updateDate(first, binding.pickupDateEditText, binding.pickupTimeEditText, true)
			val second = DateTime(it.second, DateTimeZone.UTC)
			updateDate(second, binding.returnDateEditText, binding.returnTimeEditText, false)
		}
		return picker
	}
	
	private fun setupTimePicker(titleResId: Int, isPickup: Boolean): MaterialTimePicker {
		val picker = MaterialTimePicker.Builder()
			.setTimeFormat(TimeFormat.CLOCK_24H)
			.setHour(13)
			.setMinute(0)
			.setTitleText(titleResId)
			.build()
		picker.addOnPositiveButtonClickListener {
			val date = DateTime(1, 1, 1, picker.hour, picker.minute, DateTimeZone.UTC)
			if (isPickup) {
				updateTime(date, binding.pickupDateEditText, binding.pickupTimeEditText, isPickup)
			} else {
				updateTime(date, binding.returnDateEditText, binding.returnTimeEditText, isPickup)
			}
		}
		return picker
	}
	
	private fun updateDate(
		value: DateTime,
		dateEditText: TextInputEditText,
		timeEditText: TextInputEditText,
		isPickup: Boolean
	) {
		val date =
			DateTime(value.year, value.monthOfYear, value.dayOfMonth, 0, 0, DateTimeZone.UTC)
		val time = DateHelper.getDateTime(
			timeEditText.text.toString(),
			DateHelper.TIME_FORMAT
		)
		dateEditText.setText(date.toString(DateHelper.DATE_FORMAT))
		selectDate(date, time, isPickup)
	}
	
	private fun updateTime(
		value: DateTime, dateEditText: TextInputEditText,
		timeEditText: TextInputEditText,
		isPickup: Boolean
	) {
		val time =
			DateTime(1, 1, 1, value.hourOfDay, value.minuteOfHour, DateTimeZone.UTC)
		val date = DateHelper.getDateTime(dateEditText.text.toString(), DateHelper.DATE_FORMAT)
		timeEditText.setText(time.toString(DateHelper.TIME_FORMAT))
		selectDate(date, time, isPickup)
	}
	
	private fun selectDate(date: DateTime?, time: DateTime?, isPickup: Boolean) {
		if (date != null && time != null) {
			val dateTime = DateTime(
				date.year,
				date.monthOfYear,
				date.dayOfMonth,
				time.hourOfDay,
				time.minuteOfHour,
				DateTimeZone.UTC
			)
			if (isPickup) {
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