package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.asta.hochschule.trier.verleih.databinding.ActivityNewRentalBinding

class NewRentalActivity : AppCompatActivity() {
	
	private lateinit var binding: ActivityNewRentalBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityNewRentalBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		binding.appbar.setNavigationOnClickListener {
			onBackPressed()
			finish()
		}
	}
	
}