package de.asta.hochschule.trier.verleih.rental.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.model.*
import org.joda.time.DateTime

class NewRentalViewModel : ViewModel() {
	
	private val mutableRental = MutableLiveData<Rental>()
	val rentalLiveData: LiveData<Rental> get() = mutableRental
	
	fun addRentalObject(rentalObject: RentalObject) {
		val rental = getRental()
		if (rental?.objects == null) {
			rental?.objects = mutableMapOf()
		}
		rentalObject.name?.let { rental?.objects?.put(it, mutableMapOf()) }
		mutableRental.value = rental
		
		Log.d(TAG, Gson().toJson(mutableRental.value))
	}
	
	fun removeRentalObject(rentalObject: RentalObject) {
		val rental = getRental()
		rentalObject.name?.let { rental?.objects?.remove(it) }
		mutableRental.value = rental
		
		Log.d(TAG, Gson().toJson(mutableRental.value))
	}
	
	fun enterEventTitle(text: String) {
		val rental = getRental()
		rental?.eventname = text
		mutableRental.value = rental
	}
	
	fun selectPickupDate(date: DateTime) {
		val rental = getRental()
		rental?.pickupdate = date.toString(DateHelper.TIMESTAMP_FORMAT)
		mutableRental.value = rental
	}
	
	fun selectReturnDate(date: DateTime) {
		val rental = getRental()
		rental?.returndate = date.toString(DateHelper.TIMESTAMP_FORMAT)
		mutableRental.value = rental
	}
	
	private fun getRental(): Rental? {
		return if (mutableRental.value != null) {
			mutableRental.value
		} else {
			Rental()
		}
	}
	
	companion object {
		private const val TAG = "NewRentalViewModel"
	}
}