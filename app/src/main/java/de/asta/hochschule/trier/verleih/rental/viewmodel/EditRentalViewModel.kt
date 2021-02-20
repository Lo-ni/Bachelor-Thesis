package de.asta.hochschule.trier.verleih.rental.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.rental.model.*

class EditRentalViewModel : ViewModel() {
	
	fun receiveRentalObjects(
		rental: Rental?,
		allRentalObjectsReceived: (ArrayList<RentalObject>) -> Unit
	) {
		val rentalObjects = ArrayList<RentalObject>()
		rental?.objects?.forEach { o ->
			val childrenReference = FirebaseDatabase.getInstance().reference.child("objects")
				.orderByChild("picture_name").equalTo(o.key).limitToFirst(1)
			childrenReference.get().addOnSuccessListener { allChildren ->
				allChildren.children.forEach { child ->
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
	
	companion object {
		private const val TAG = "EditRentalViewModel"
	}
}