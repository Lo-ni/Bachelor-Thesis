package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalItemsQuanityBinding
import de.asta.hochschule.trier.verleih.rental.adapter.RentalItemQuantityAdapter
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel

class NewRentalItemsQuantityFragment : Fragment(R.layout.fragment_new_rental_items_quanity) {
	
	private lateinit var binding: FragmentNewRentalItemsQuanityBinding
	
	private val viewModel: NewRentalViewModel by activityViewModels()
	
	private var adapter: RentalItemQuantityAdapter? = null
	
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
		
		binding.itemsRecyclerview.layoutManager = LinearLayoutManager(context)
		adapter = RentalItemQuantityAdapter(viewModel.objectsLiveData.value) {
			if (it != null) {
				viewModel.removeRentalObject(it)
			}
		}
		binding.itemsRecyclerview.adapter = adapter
		
		viewModel.objectsLiveData.observe(requireActivity(), { objects ->
			adapter = adapter?.resetObjects(objects)
		})
	}
	
	companion object {
		private const val TAG = "NewRentalItemsQuantityFragment"
	}
}