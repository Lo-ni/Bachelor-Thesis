package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalItemsQuanityBinding

class NewRentalItemsQuantityFragment : Fragment(R.layout.fragment_new_rental_items_quanity) {
	
	private lateinit var binding: FragmentNewRentalItemsQuanityBinding
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNewRentalItemsQuanityBinding.inflate(layoutInflater)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}
	
	companion object {
		private const val TAG = "NewRentalItemsQuantityFragment"
	}
}