package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.*
import de.asta.hochschule.trier.verleih.rental.adapter.RentalItemChoiceAdapter
import de.asta.hochschule.trier.verleih.rental.model.*
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel
import de.asta.hochschule.trier.verleih.util.Constants

class NewRentalItemsChoiceFragment : Fragment(R.layout.fragment_new_rental_items_choice) {
	
	private lateinit var binding: FragmentNewRentalItemsChoiceBinding
	private lateinit var bottomSheetBinding: BottomSheetItemInfoBinding
	private lateinit var adapter: RentalItemChoiceAdapter
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
		
		val bottomSheetDialog = context?.let { BottomSheetDialog(it) }
		bottomSheetDialog?.setContentView(bottomSheetBinding.root)
		setupRecyclerView(bottomSheetDialog)
		
		viewModel.objectsLiveData.observe(requireActivity(), {
			adapter = adapter.resetSelectedItems(it)
		})
		viewModel.validPagesLiveData.observe(viewLifecycleOwner, {
			if (!it[NewRentalActivity.PAGE_ITEMS_CHOICE]) {
				Toast.makeText(context, R.string.rental_items_choice_error, Toast.LENGTH_SHORT)
					.show()
			}
		})
	}
	
	private fun setupRecyclerView(bottomSheetDialog: BottomSheetDialog?) {
		val parser = SnapshotParser { snapshot ->
			val obj = snapshot.getValue(RentalObject::class.java)
			if (obj != null) {
				obj.id = snapshot.key
				return@SnapshotParser obj
			}
			return@SnapshotParser RentalObject()
		}
		val query = FirebaseDatabase.getInstance().reference.child(Constants.OBJECTS.childName)
			.orderByChild(Constants.NAME.childName)
		val options =
			FirebaseRecyclerOptions.Builder<RentalObject>()
				.setQuery(query, parser)
				.build()
		
		adapter =
			RentalItemChoiceAdapter(options, viewModel.objectsLiveData.value, { model, isSelected ->
				if (isSelected) {
					context?.getString(R.string.quantity)
						?.let { viewModel.addRentalObject(model, it) }
				} else {
					viewModel.removeRentalObject(model)
				}
			}, { model ->
				bottomSheetBinding.itemTitleText.text = model.name
				bottomSheetBinding.itemDescriptionText.text = model.description
				bottomSheetDialog?.show()
			})
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
	
}