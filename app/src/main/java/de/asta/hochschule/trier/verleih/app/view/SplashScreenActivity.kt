package de.asta.hochschule.trier.verleih.app.view

import android.content.Intent
import android.os.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.app.viewmodel.SplashScreenViewModel
import de.asta.hochschule.trier.verleih.signIn.activity.SignInActivity

class SplashScreenActivity : AppCompatActivity() {
	
	private val viewModel: SplashScreenViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)
		
		FirebaseApp.initializeApp(this)
	}
	
	override fun onResume() {
		super.onResume()
		
		Handler(Looper.getMainLooper()).postDelayed({
			val intent = if (viewModel.isLoggedIn()) {
				Intent(this@SplashScreenActivity, MainActivity::class.java)
			} else {
				Intent(this@SplashScreenActivity, SignInActivity::class.java)
			}
			intent.flags = intent.flags or Intent.FLAG_ACTIVITY_CLEAR_TOP
			startActivity(intent)
			finish()
		}, SPLASH_TIME.toLong())
	}
	
	companion object {
		private const val SPLASH_TIME = 1500 // = 1.5 seconds
	}
	
}