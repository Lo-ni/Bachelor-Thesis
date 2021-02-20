package de.asta.hochschule.trier.verleih.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.app.viewmodel.MainViewModel
import de.asta.hochschule.trier.verleih.databinding.ActivityMainBinding
import de.asta.hochschule.trier.verleih.rental.model.Rental
import de.asta.hochschule.trier.verleih.rental.view.EditRentalActivity

class MainActivity : AppCompatActivity() {
	
	private lateinit var binding: ActivityMainBinding
	
	val viewModel: MainViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
		val navController =
			supportFragmentManager.findFragmentById(R.id.main_nav_host)?.findNavController()
		if (navController != null) {
			NavigationUI.setupWithNavController(bottomNavigationView, navController)
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == EditRentalActivity.DELETE_RENTAL_REQUEST_CODE) {
			if (resultCode == EditRentalActivity.DELETE_RENTAL_REQUEST_CODE && data != null) {
				val rental = Gson().fromJson(
					data.getStringExtra(EditRentalActivity.INTENT_EXTRA_DELETE_RENTAL),
					Rental::class.java
				)
				viewModel.deleteRental(rental)
				
				val snackBar = Snackbar.make(
					binding.root,
					"${rental.eventname} ${getString(R.string.deleted)}",
					Snackbar.LENGTH_LONG
				)
				snackBar.setAction(R.string.undo) {
					viewModel.reAddRental(rental)
				}
				snackBar.show()
			}
		}
	}
	
	companion object {
		private const val TAG = "MainActivty"
	}
	
}