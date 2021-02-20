package de.asta.hochschule.trier.verleih.rental.view

import android.content.Intent
import android.os.Bundle
import android.view.*
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
		
		val isEditable = rental?.pickupdate?.let { DateHelper.getDateTime(it).isAfterNow }
		if (isEditable == true) {
			binding.appbar.inflateMenu(R.menu.appbar_edit_delete)
		}
		
		binding.rentalContainer.itemsRecyclerview.layoutManager = LinearLayoutManager(this)
		viewModel.receiveRentalObjects(rental) {
			binding.rentalContainer.itemsRecyclerview.adapter =
				RentalItemOverviewAdapter(it, rental?.objects)
			if (isEditable == true) {
				binding.appbar.setOnMenuItemClickListener { item ->
					when (item.itemId) {
						R.id.appbar_edit -> {
							val intent = Intent(this, NewRentalActivity::class.java)
							intent.putExtra(INTENT_EXTRA_RENTAL, Gson().toJson(rental))
							intent.putExtra(INTENT_EXTRA_RENTAL_OBJECTS, Gson().toJson(it))
							this.startActivityForResult(intent, EDIT_RENTAL_REQUEST_CODE)
							return@setOnMenuItemClickListener true
						}
						R.id.appbar_delete -> {
							// TODO
							finish()
							return@setOnMenuItemClickListener true
						}
						else -> return@setOnMenuItemClickListener false
					}
				}
			}
		}
		
		binding.rentalContainer.noteTitleText.text = getString(R.string.notes)
		binding.rentalContainer.noteDescription.visibility = View.GONE
		binding.rentalContainer.notesRecyclerView.visibility = View.VISIBLE
		binding.rentalContainer.notesRecyclerView.layoutManager = LinearLayoutManager(this)
		binding.rentalContainer.notesRecyclerView.adapter = EditRentalNotesAdapter(rental?.notes)
		binding.rentalContainer.noteTextInputLayout.hint = getString(R.string.add_note)
		
		binding.rentalContainer.editItemsButton.visibility = View.INVISIBLE
		binding.rentalContainer.editInformationButton.visibility = View.INVISIBLE
		
	}
	
	companion object {
		const val INTENT_EXTRA_RENTAL = "Rental"
		const val INTENT_EXTRA_RENTAL_OBJECTS = "Rental_Objects"
		private const val TAG = "EditRentalActivity"
		private const val EDIT_RENTAL_REQUEST_CODE = 1
	}
}