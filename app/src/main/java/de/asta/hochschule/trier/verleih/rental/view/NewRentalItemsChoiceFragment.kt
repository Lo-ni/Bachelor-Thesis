package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.*
import de.asta.hochschule.trier.verleih.rental.adapter.RentalItemChoiceAdapter
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel

class NewRentalItemsChoiceFragment : Fragment(R.layout.fragment_new_rental_items_choice) {
	
	private lateinit var binding: FragmentNewRentalItemsChoiceBinding
	private lateinit var bottomSheetBinding: BottomSheetItemInfoBinding
	
	private lateinit var adapter: RentalItemChoiceAdapter
	private var bottomSheetDialog: BottomSheetDialog? = null
	
	private val viewModel: NewRentalViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNewRentalItemsChoiceBinding.inflate(layoutInflater)
		bottomSheetBinding = BottomSheetItemInfoBinding.inflate(layoutInflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		bottomSheetDialog = context?.let { BottomSheetDialog(it) }
		bottomSheetDialog?.setContentView(bottomSheetBinding.root)
		
		val query =
			FirebaseDatabase.getInstance().reference.child("objects").orderByChild("name")
		val options =
			FirebaseRecyclerOptions.Builder<RentalObject>()
				.setQuery(query, RentalObject::class.java)
				.build()
		adapter = RentalItemChoiceAdapter(options, { model ->
			bottomSheetBinding.itemTitleText.text = model.name
			bottomSheetBinding.itemDescriptionText.text = model.description
			bottomSheetDialog?.show()
		}, { model, isSelected ->
			if (isSelected) {
				viewModel.addRentalObject(model)
			} else {
				viewModel.removeRentalObject(model)
			}
		}, viewModel.objectsLiveData.value)
		binding.itemsRecyclerview.layoutManager = GridLayoutManager(this.context, 3)
		binding.itemsRecyclerview.adapter = adapter
		
		viewModel.objectsLiveData.observe(requireActivity(), {
			adapter = adapter.resetSelectedItems(it)
		})
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