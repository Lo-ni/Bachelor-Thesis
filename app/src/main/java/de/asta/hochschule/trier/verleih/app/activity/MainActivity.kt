package de.asta.hochschule.trier.verleih.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	
	lateinit var binding: ActivityMainBinding
	
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
	
}