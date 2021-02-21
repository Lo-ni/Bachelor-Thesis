package de.asta.hochschule.trier.verleih.rental.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.app.util.Constants
import de.asta.hochschule.trier.verleih.rental.model.*

class EditRentalViewModel : ViewModel() {
	
	fun receiveRentalObjects(
		rental: Rental?,
		allRentalObjectsReceived: (ArrayList<RentalObject>) -> Unit
	) {
		val rentalObjects = ArrayList<RentalObject>()
		rental?.objects?.forEach { obj ->
			val query =
				FirebaseDatabase.getInstance().reference.child(Constants.OBJECTS.childName)
					.orderByChild(Constants.PICTURE_NAME.childName).equalTo(obj.key).limitToFirst(1)
			query.get().addOnSuccessListener { children ->
				children.children.forEach { child ->
					val rentalObject = child.getValue(RentalObject::class.java)
					if (rentalObject != null) {
						rentalObjects.add(rentalObject)
					}
				}
			}.addOnFailureListener { Log.e(TAG, it.message.toString()) }.addOnCompleteListener {
				allRentalObjectsReceived.invoke(rentalObjects)
			}
		}
	}
	
	fun addRentalNote(rental: Rental?, note: String, completeUpdate: (Rental) -> Unit) {
		val firebaseRef =
			rental?.id?.let {
				FirebaseDatabase.getInstance().reference.child(Constants.RENTALS.childName)
					.child(it).child(Constants.NOTES.childName)
			}
		
		val timestamp = System.currentTimeMillis().toString()
		if (rental?.notes == null) {
			rental?.notes = mutableMapOf()
		}
		rental?.notes?.put(timestamp, note)
		rental?.notes?.toMap()?.let {
			firebaseRef?.updateChildren(it) { error, _ ->
				if (error != null) {
					Log.e(TAG, error.message)
				} else {
					completeUpdate.invoke(rental)
				}
			}
		}
		
	}
	
	companion object {
		private const val TAG = "EditRentalViewModel"
	}
}