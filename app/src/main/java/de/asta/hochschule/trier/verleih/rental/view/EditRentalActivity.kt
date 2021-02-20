package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.ActivityEditRentalBinding
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.adapter.*
import de.asta.hochschule.trier.verleih.rental.model.Rental
import de.asta.hochschule.trier.verleih.rental.viewmodel.EditRentalViewModel

class EditRentalActivity : FragmentActivity() {
	
	private lateinit var binding: ActivityEditRentalBinding
	
	private var rental: Rental? = null
	
	private val viewModel: EditRentalViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityEditRentalBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		rental = Gson().fromJson(intent.getStringExtra(INTENT_EXTRA_RENTAL), Rental::class.java)
		
		binding.appbar.setNavigationOnClickListener {
			onBackPressed()
		}
		
		binding.rentalContainer.overviewDescription.visibility = View.GONE
		binding.rentalContainer.statusText.visibility = View.VISIBLE
		binding.rentalContainer.statusDescription.visibility = View.VISIBLE
		
		binding.rentalContainer.eventTitleText.text = rental?.eventname
		binding.rentalContainer.statusText.text = rental?.status
		binding.rentalContainer.eventPickupText.text =
			rental?.pickupdate?.let {
				DateHelper.getDateTime(it).toString(DateHelper.LONG_DATE_TIME_FORMAT)
			}
		binding.rentalContainer.eventReturnText.text =
			rental?.returndate?.let {
				DateHelper.getDateTime(it).toString(DateHelper.LONG_DATE_TIME_FORMAT)
			}
		
		binding.rentalContainer.itemsRecyclerview.layoutManager = LinearLayoutManager(this)
		viewModel.receiveRentalObjects(rental) {
			binding.rentalContainer.itemsRecyclerview.adapter =
				RentalItemOverviewAdapter(it, rental?.objects)
		}
		
		binding.rentalContainer.noteTitleText.text = getString(R.string.notes)
		binding.rentalContainer.noteDescription.visibility = View.GONE
		binding.rentalContainer.notesRecyclerView.visibility = View.VISIBLE
		binding.rentalContainer.notesRecyclerView.layoutManager = LinearLayoutManager(this)
		binding.rentalContainer.notesRecyclerView.adapter = EditRentalNotesAdapter(rental?.notes)
		binding.rentalContainer.noteTextInputLayout.hint = getString(R.string.add_note)
		
		val isEditable = rental?.pickupdate?.let { DateHelper.getDateTime(it).isAfterNow }
		if (isEditable == false) {
			binding.rentalContainer.editItemsButton.visibility = View.INVISIBLE
			binding.rentalContainer.editInformationButton.visibility = View.INVISIBLE
		}
	}
	
	companion object {
		const val INTENT_EXTRA_RENTAL = "Rental"
		private const val TAG = "EditRentalActivity"
	}
}