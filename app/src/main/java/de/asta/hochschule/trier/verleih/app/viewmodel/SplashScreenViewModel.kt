package de.asta.hochschule.trier.verleih.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashScreenViewModel : ViewModel() {
	
	fun isLoggedIn(): Boolean {
		return FirebaseAuth.getInstance().currentUser != null
	}
	
}