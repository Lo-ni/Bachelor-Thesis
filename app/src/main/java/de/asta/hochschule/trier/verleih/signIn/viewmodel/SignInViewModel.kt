package de.asta.hochschule.trier.verleih.signIn.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.*
import de.asta.hochschule.trier.verleih.R

class SignInViewModel : ViewModel() {
	
	fun getSignInIntent(): Intent {
		val authProviders = arrayListOf(
			AuthUI.IdpConfig.EmailBuilder().build(),
			AuthUI.IdpConfig.GoogleBuilder().build()
		)
		val authLayout = AuthMethodPickerLayout.Builder(R.layout.activity_sign_in)
			.setGoogleButtonId(R.id.sign_in_google_button)
			.setEmailButtonId(R.id.sign_in_mail_button).build()
		return AuthUI.getInstance().createSignInIntentBuilder()
			.setAvailableProviders(authProviders)
			.setAuthMethodPickerLayout(authLayout)
			.setTheme(R.style.FirebaseTheme)
			.build()
	}
}