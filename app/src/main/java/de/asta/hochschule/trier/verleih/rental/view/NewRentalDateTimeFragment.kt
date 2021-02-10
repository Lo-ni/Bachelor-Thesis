package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.*
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalDateTimeBinding
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.util.DateValidatorWeekdays
import org.joda.time.*
import java.util.*
import kotlin.collections.ArrayList

class NewRentalDateTimeFragment : Fragment(R.layout.fragment_new_rental_date_time) {
	
	private lateinit var binding: FragmentNewRentalDateTimeBinding
	
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
		
		val todayMS = MaterialDatePicker.todayInUtcMilliseconds()
		val nextSixMonthCal = Calendar.getInstance(TIMEZONE_UTC)
		nextSixMonthCal.roll(Calendar.MONTH, 6)
		val validators = ArrayList<CalendarConstraints.DateValidator>()
		validators.add(DateValidatorWeekdays())
		validators.add(DateValidatorPointForward.now())
		val datePicker = MaterialDatePicker.Builder.dateRangePicker()
			.setTitleText(R.string.rental_time_range)
			.setCalendarConstraints(
				CalendarConstraints.Builder()
					.setStart(todayMS)
					.setEnd(nextSixMonthCal.timeInMillis)
					.setValidator(CompositeDateValidator.allOf(validators))
					.build()
			).build()
		datePicker.addOnPositiveButtonClickListener {
			val pickupDate = DateTime(it.first, DateTimeZone.UTC)
			val returnDate = DateTime(it.second, DateTimeZone.UTC)
			binding.pickupDateEditText.setText(pickupDate.toString(DateHelper.DATE_FORMAT))
			binding.returnDateEditText.setText(returnDate.toString(DateHelper.DATE_FORMAT))
		}
		
		binding.pickupDateEditText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> datePicker.show(fm, TAG) }
		}
		
		binding.returnDateEditText.setOnClickListener {
			activity?.supportFragmentManager?.let { fm -> datePicker.show(fm, TAG) }
		}
	}
	
	companion object {
		private const val TAG = "NewRentalDateTimeFragment"
		private val TIMEZONE_UTC = TimeZone.getTimeZone("UTC")
	}
}