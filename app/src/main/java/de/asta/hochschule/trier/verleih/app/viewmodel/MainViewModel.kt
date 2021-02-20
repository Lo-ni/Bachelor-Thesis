package de.asta.hochschule.trier.verleih.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.rental.model.Rental

class MainViewModel : ViewModel() {
	
	fun deleteRental(rental: Rental) {
		rental.id?.let {
			FirebaseDatabase.getInstance().reference.child("rentals").child(it).removeValue()
		}
	}
	
	fun reAddRental(rental: Rental) {
		rental.id = null
		val firebaseRef = FirebaseDatabase.getInstance().reference.child("rentals")
		firebaseRef.push().setValue(rental)
	}
	
}