package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalOverviewBinding

class NewRentalOverviewFragment : Fragment(R.layout.fragment_new_rental_overview) {
	
	private lateinit var binding: FragmentNewRentalOverviewBinding
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNewRentalOverviewBinding.inflate(layoutInflater)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}
	
	companion object {
		private const val TAG = "NewRentalOverviewFragment"
	}
}