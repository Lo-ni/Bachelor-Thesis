package de.asta.hochschule.trier.verleih.rental.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.*
import com.google.gson.Gson
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.ActivityEditRentalBinding
import de.asta.hochschule.trier.verleih.rental.adapter.*
import de.asta.hochschule.trier.verleih.rental.model.*
import de.asta.hochschule.trier.verleih.rental.viewmodel.EditRentalViewModel
import de.asta.hochschule.trier.verleih.util.DateHelper

class EditRentalActivity : FragmentActivity() {
	
	private lateinit var binding: ActivityEditRentalBinding
	
	private val viewModel: EditRentalViewModel by viewModels()
	
	private var rental: Rental? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityEditRentalBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		rental = Gson().fromJson(intent.getStringExtra(INTENT_EXTRA_RENTAL), Rental::class.java)
		val isEditable = rental?.pickupdate?.let { DateHelper.getDateTime(it).isAfterNow }
		
		setupAppBar(isEditable)
		setupRecyclerViews(isEditable)
		setViewsVisibility()
		setTexts()
	}
	
	private fun setupRecyclerViews(showMenuItems: Boolean?) {
		binding.rentalContainer.itemsRecyclerview.layoutManager = LinearLayoutManager(this)
		viewModel.receiveRentalObjects(rental) { objects ->
			binding.rentalContainer.itemsRecyclerview.adapter =
				RentalItemOverviewAdapter(objects, rental?.objects)
			if (showMenuItems == true) {
				binding.appbar.setOnMenuItemClickListener(getOnMenuItemClickListener(objects))
			}
		}
		
		binding.rentalContainer.notesRecyclerView.layoutManager = LinearLayoutManager(this)
		binding.rentalContainer.notesRecyclerView.adapter = EditRentalNotesAdapter(rental?.notes)
	}
	
	private fun setupAppBar(showMenuItems: Boolean?) {
		binding.appbar.setNavigationOnClickListener {
			onBackPressed()
		}
		if (showMenuItems == true) {
			binding.appbar.inflateMenu(R.menu.appbar_edit_delete)
		}
	}
	
	private fun setTexts() {
		binding.rentalContainer.eventTitleText.text = rental?.eventname
		binding.rentalContainer.statusText.text = rental?.status
		binding.rentalContainer.noteTitleText.text = getString(R.string.notes)
		binding.rentalContainer.noteTextInputLayout.hint = getString(R.string.add_note)
		binding.rentalContainer.eventPickupText.text =
			rental?.pickupdate?.let {
				DateHelper.getDateTime(it).toString(DateHelper.LONG_DATE_TIME_FORMAT)
			}
		binding.rentalContainer.eventReturnText.text =
			rental?.returndate?.let {
				DateHelper.getDateTime(it).toString(DateHelper.LONG_DATE_TIME_FORMAT)
			}
	}
	
	private fun setViewsVisibility() {
		binding.rentalContainer.overviewDescription.visibility = View.GONE
		binding.rentalContainer.statusText.visibility = View.VISIBLE
		binding.rentalContainer.statusDescription.visibility = View.VISIBLE
		binding.rentalContainer.noteDescription.visibility = View.GONE
		binding.rentalContainer.notesRecyclerView.visibility = View.VISIBLE
		binding.rentalContainer.editItemsButton.visibility = View.INVISIBLE
		binding.rentalContainer.editInformationButton.visibility = View.INVISIBLE
	}
	
	private fun getOnMenuItemClickListener(rentalObjects: ArrayList<RentalObject>): Toolbar.OnMenuItemClickListener {
		return Toolbar.OnMenuItemClickListener { item ->
			when (item.itemId) {
				R.id.appbar_edit -> {
					rental?.let { editRental(it, rentalObjects) }
					return@OnMenuItemClickListener true
				}
				R.id.appbar_delete -> {
					rental?.let { deleteRental(it) }
					return@OnMenuItemClickListener true
				}
				else -> return@OnMenuItemClickListener false
			}
		}
	}
	
	private fun editRental(rental: Rental, rentalObjects: ArrayList<RentalObject>) {
		val intent = Intent(this, NewRentalActivity::class.java)
		intent.putExtra(INTENT_EXTRA_RENTAL, Gson().toJson(rental))
		intent.putExtra(INTENT_EXTRA_RENTAL_OBJECTS, Gson().toJson(rentalObjects))
		this.startActivityForResult(intent, EDIT_RENTAL_REQUEST_CODE)
	}
	
	private fun deleteRental(rental: Rental) {
		val i = intent
		i.putExtra(INTENT_EXTRA_DELETE_RENTAL, Gson().toJson(rental))
		setResult(DELETE_RENTAL_REQUEST_CODE, i)
		finish()
	}
	
	companion object {
		const val INTENT_EXTRA_RENTAL = "Rental"
		const val INTENT_EXTRA_RENTAL_OBJECTS = "Rental_Objects"
		const val INTENT_EXTRA_DELETE_RENTAL = "Delete_Rental"
		private const val EDIT_RENTAL_REQUEST_CODE = 1
		const val DELETE_RENTAL_REQUEST_CODE = 2
	}
	
}