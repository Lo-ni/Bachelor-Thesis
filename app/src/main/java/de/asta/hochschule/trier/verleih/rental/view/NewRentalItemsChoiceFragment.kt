package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalItemsChoiceBinding
import de.asta.hochschule.trier.verleih.rental.adapter.RentalItemChoiceAdapter
import de.asta.hochschule.trier.verleih.rental.model.RentalObject

class NewRentalItemsChoiceFragment : Fragment(R.layout.fragment_new_rental_items_choice) {
	
	private lateinit var binding: FragmentNewRentalItemsChoiceBinding
	
	private lateinit var adapter: RentalItemChoiceAdapter
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNewRentalItemsChoiceBinding.inflate(layoutInflater)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		val query =
			FirebaseDatabase.getInstance().reference.child("objects")//.orderByChild("name").startAt("A")
		val options =
			FirebaseRecyclerOptions.Builder<RentalObject>()
				.setQuery(query, RentalObject::class.java)
				.build()
		adapter = RentalItemChoiceAdapter(options)
		binding.itemsRecyclerview.layoutManager = GridLayoutManager(this.context, 3)
		
		binding.itemsRecyclerview.adapter = adapter
	}
	
	override fun onStart() {
		super.onStart()
		adapter.startListening()
	}
	
	override fun onStop() {
		super.onStop()
		adapter.stopListening()
	}
	
	companion object {
		private const val TAG = "NewRentalItemsChoiceFragment"
	}
}