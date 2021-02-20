package de.asta.hochschule.trier.verleih.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.rental.model.Rental
import de.asta.hochschule.trier.verleih.util.Constants

class MainViewModel : ViewModel() {
	
	fun deleteRental(rental: Rental) {
		rental.id?.let {
			FirebaseDatabase.getInstance().reference.child(Constants.RENTALS.childName)
				.child(it).removeValue()
		}
	}
	
	fun reAddRental(rental: Rental) {
		rental.id = null
		FirebaseDatabase.getInstance().reference.child(Constants.RENTALS.childName)
			.push().setValue(rental)
	}
	
}