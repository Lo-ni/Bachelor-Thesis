package de.asta.hochschule.trier.verleih.rental.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.rental.viewmodel.RentalMainViewModel

class RentalMainFragment : Fragment(R.layout.fragment_rental_main) {
	
	private val viewModel: RentalMainViewModel by lazy {
		ViewModelProvider(this).get(RentalMainViewModel::class.java)
	}
	
}