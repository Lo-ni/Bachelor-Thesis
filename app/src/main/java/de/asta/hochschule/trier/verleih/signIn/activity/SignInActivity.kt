package de.asta.hochschule.trier.verleih.signIn.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import de.asta.hochschule.trier.verleih.app.view.MainActivity
import de.asta.hochschule.trier.verleih.signIn.viewmodel.SignInViewModel

class SignInActivity : AppCompatActivity() {
	
	private val viewModel: SignInViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		startActivityForResult(viewModel.getSignInIntent(), RC_SIGN_IN)
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		Log.d(TAG, "$requestCode $resultCode $data")
		
		if (requestCode == RC_SIGN_IN) {
			val response = IdpResponse.fromResultIntent(data)
			if (resultCode == RESULT_OK) {
				val user = FirebaseAuth.getInstance().currentUser
				Log.d(TAG, "Logged in user ${user?.displayName}")
				startActivity(Intent(this, MainActivity::class.java))
				finish()
			} else {
				// sign in failed
				if (response == null) {
					// user cancelled the sign-in flow using the back button
					Log.d(TAG, "Sign In has been canceled")
					Toast.makeText(this, "Bitte beende den Login-Vorgang", Toast.LENGTH_SHORT)
						.show()
					startActivityForResult(viewModel.getSignInIntent(), RC_SIGN_IN)
				} else {
					// handle the error
					Log.e(TAG, "Sign In failed", response.error)
					Toast.makeText(
						this,
						"Login fehlgeschlagen. Bitte versuche es erneut.",
						Toast.LENGTH_SHORT
					).show()
					startActivityForResult(viewModel.getSignInIntent(), RC_SIGN_IN)
				}
			}
		}
	}
	
	companion object {
		private val TAG = this::class.java.name
		private const val RC_SIGN_IN = 9001
	}
}