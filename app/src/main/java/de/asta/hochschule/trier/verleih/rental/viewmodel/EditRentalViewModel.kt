package de.asta.hochschule.trier.verleih.rental.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.rental.model.*
import de.asta.hochschule.trier.verleih.util.Constants

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
	
	companion object {
		private const val TAG = "EditRentalViewModel"
	}
}