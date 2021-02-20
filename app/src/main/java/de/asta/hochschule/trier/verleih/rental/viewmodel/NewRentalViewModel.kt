package de.asta.hochschule.trier.verleih.rental.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.model.*
import org.joda.time.DateTime

class NewRentalViewModel : ViewModel() {
	
	private val mutableRental = MutableLiveData<Rental>()
	val rentalLiveData: LiveData<Rental> get() = mutableRental
	private val mutableObjects = MutableLiveData<ArrayList<RentalObject>>()
	val objectsLiveData: LiveData<ArrayList<RentalObject>> get() = mutableObjects
	private val mutableRentalObjects =
		MutableLiveData<MutableMap<String, MutableMap<String, Int>?>>()
	val rentalObjectsLiveData: LiveData<MutableMap<String, MutableMap<String, Int>?>> get() = mutableRentalObjects
	private val mutableNote = MutableLiveData<String>()
	val noteLiveData: LiveData<String> get() = mutableNote
	
	fun buildRental(): Rental? {
		val rental = getRental()
		val rentalObjects = getRentalObjects()
		val note = mutableNote.value
		val timestamp = System.currentTimeMillis().toString()
		
		rental?.objects = rentalObjects
		if (note != null) {
			if (rental?.notes == null) {
				rental?.notes = mutableMapOf()
			}
			rental?.notes?.put(timestamp, note)
		}
		rental?.timestamp = timestamp
		rental?.status = "In Bearbeitung"
		
		mutableRental.value = rental
		Log.d(TAG, Gson().toJson(mutableRental.value))
		return rental
	}
	
	fun updateQuantity(rentalObject: RentalObject, component: Pair<String, Int>, quantity: Int) {
		val rentalObjects = getRentalObjects()
		if (rentalObjects?.containsKey(rentalObject.picture_name) == true) {
			if (rentalObjects[rentalObject.picture_name]?.containsKey(component.first) == true) {
				rentalObjects[rentalObject.picture_name]?.replace(component.first, quantity)
			} else {
				rentalObjects[rentalObject.picture_name]?.put(component.first, quantity)
			}
		} else {
			rentalObject.picture_name?.let {
				rentalObjects?.put(it, mutableMapOf(Pair(component.first, quantity)))
			}
		}
		mutableRentalObjects.value = rentalObjects
	}
	
	fun addRentalObject(rentalObject: RentalObject, context: Context?) {
		val list = getObjectList()
		if (list?.contains(rentalObject) == false) {
			list.add(rentalObject)
			list.sortBy { it.name }
			mutableObjects.value = list
		}
		
		val rentalObjects = getRentalObjects()
		if (rentalObjects?.containsKey(rentalObject.picture_name) == false) {
			val newMutableMap = mutableMapOf<String, Int>()
			if (rentalObject.components == null) {
				context?.getString(R.string.quantity)?.let { newMutableMap.put(it, 0) }
			} else {
				rentalObject.components?.map { component ->
					newMutableMap.put(component.key, 0)
				}
			}
			rentalObject.picture_name?.let { rentalObjects.put(it, newMutableMap) }
			mutableRentalObjects.value = rentalObjects
		}
	}
	
	fun removeRentalObject(rentalObject: RentalObject) {
		val list = getObjectList()
		list?.remove(rentalObject)
		list?.sortBy { it.name }
		mutableObjects.value = list
		
		val rentalObjects = getRentalObjects()
		rentalObjects?.remove(rentalObject.picture_name)
		mutableRentalObjects.value = rentalObjects
	}
	
	fun enterEventNote(text: String) {
		mutableNote.value = text
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
	
	private fun getRentalObjects(): MutableMap<String, MutableMap<String, Int>?>? {
		return if (mutableRentalObjects.value != null) {
			mutableRentalObjects.value
		} else {
			mutableMapOf()
		}
	}
	
	fun setupViewModel(rental: Rental, rentalObjects: ArrayList<RentalObject>) {
		mutableRental.value = rental
		mutableRentalObjects.value = rental.objects
		mutableObjects.value = rentalObjects
	}
	
	companion object {
		private const val TAG = "NewRentalViewModel"
	}
}