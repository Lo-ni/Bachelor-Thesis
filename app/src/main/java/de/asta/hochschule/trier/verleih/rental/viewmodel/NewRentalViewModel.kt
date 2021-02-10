package de.asta.hochschule.trier.verleih.rental.viewmodel

import androidx.lifecycle.*
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.model.*
import org.joda.time.DateTime

class NewRentalViewModel : ViewModel() {
	
	private val mutableRental = MutableLiveData<Rental>()
	val rentalLiveData: LiveData<Rental> get() = mutableRental
	private val mutableObjects = MutableLiveData<ArrayList<RentalObject>>()
	val objectsLiveData: LiveData<ArrayList<RentalObject>> get() = mutableObjects
	
	fun addRentalObject(rentalObject: RentalObject) {
		val list = getObjectList()
		list?.add(rentalObject)
		mutableObjects.value = list
	}
	
	fun removeRentalObject(rentalObject: RentalObject) {
		val list = getObjectList()
		list?.remove(rentalObject)
		mutableObjects.value = list
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
	
	private fun getObjectList(): ArrayList<RentalObject>? {
		return if (mutableObjects.value != null) {
			mutableObjects.value
		} else {
			ArrayList()
		}
	}
	
	companion object {
		private const val TAG = "NewRentalViewModel"
	}
}