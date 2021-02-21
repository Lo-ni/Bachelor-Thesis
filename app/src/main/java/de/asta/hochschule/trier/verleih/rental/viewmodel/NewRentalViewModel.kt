package de.asta.hochschule.trier.verleih.rental.viewmodel

import androidx.lifecycle.*
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.app.util.*
import de.asta.hochschule.trier.verleih.rental.model.*
import de.asta.hochschule.trier.verleih.rental.view.NewRentalActivity
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
	private val mutableValidPages = MutableLiveData<ArrayList<Boolean>>()
	val validPagesLiveData: LiveData<ArrayList<Boolean>> get() = mutableValidPages
	
	fun saveRentalToDatabase() {
		val rental = compileRental()
		val firebaseRef =
			FirebaseDatabase.getInstance().reference.child(Constants.RENTALS.childName)
		firebaseRef.push().setValue(rental)
	}
	
	private fun compileRental(): Rental? {
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
		rental?.status = RentalState.IN_PROGRESS.state
		
		mutableRental.value = rental
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
	
	fun addRentalObject(rentalObject: RentalObject, quantityString: String) {
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
				newMutableMap[quantityString] = 0
			} else {
				rentalObject.components?.map { component ->
					newMutableMap.put(component.key, 0)
				}
			}
			rentalObject.picture_name?.let { rentalObjects.put(it, newMutableMap) }
			mutableRentalObjects.value = rentalObjects
		}
	}
	
	fun removeRentalObject(rentalObject: RentalObject?) {
		val list = getObjectList()
		list?.remove(list.find { it.name == rentalObject?.name })
		list?.sortBy { it.name }
		mutableObjects.value = list
		
		val rentalObjects = getRentalObjects()
		rentalObjects?.remove(rentalObject?.picture_name)
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
	
	private fun getValidPages(): ArrayList<Boolean>? {
		return if (mutableValidPages.value != null) {
			mutableValidPages.value
		} else {
			val validPages = ArrayList<Boolean>()
			for (i in 0 until NewRentalActivity.NUM_PAGES) {
				validPages.add(true)
			}
			validPages
		}
	}
	
	fun setupViewModel(rental: Rental, rentalObjects: ArrayList<RentalObject>) {
		mutableRental.value = rental
		mutableRentalObjects.value = rental.objects
		mutableObjects.value = rentalObjects
	}
	
	fun validateInput(currentPage: Int): Boolean {
		val isValid = when (currentPage) {
			NewRentalActivity.PAGE_DATE_TIME -> {
				mutableRental.value?.eventname != null &&
						mutableRental.value?.pickupdate != null &&
						mutableRental.value?.returndate != null
			}
			NewRentalActivity.PAGE_ITEMS_CHOICE -> {
				!(mutableObjects.value == null || mutableObjects.value?.size == 0)
			}
			NewRentalActivity.PAGE_ITEMS_QUANTITY -> {
				countValidItems() == mutableRentalObjects.value?.size
			}
			else -> true
		}
		val validPages = getValidPages()
		validPages?.set(currentPage, isValid)
		mutableValidPages.value = validPages
		return isValid
	}
	
	private fun countValidItems(): Int {
		var validItems = 0
		mutableRentalObjects.value?.forEach { obj ->
			val objValues = obj.value
			if (objValues != null) {
				for (component in objValues) {
					if (component.value > 0) {
						++validItems
						break
					}
				}
			}
		}
		return validItems
	}
	
}