package de.asta.hochschule.trier.verleih.signIn.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import de.asta.hochschule.trier.verleih.R
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
		
		if (requestCode == RC_SIGN_IN) {
			val response = IdpResponse.fromResultIntent(data)
			if (resultCode == RESULT_OK) {
				Log.d(TAG, "Logged in user ${FirebaseAuth.getInstance().currentUser?.displayName}")
				startActivity(Intent(this, MainActivity::class.java))
				finish()
			} else {
				val messageResId = if (response == null) {
					Log.d(TAG, "Sign In has been canceled")
					R.string.sign_in_canceled_message
				} else {
					Log.e(TAG, "Sign In failed", response.error)
					R.string.sign_in_error_message
				}
				Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
				startActivityForResult(viewModel.getSignInIntent(), RC_SIGN_IN)
			}
		}
	}
	
	companion object {
		private val TAG = this::class.java.name
		private const val RC_SIGN_IN = 9001
	}
}