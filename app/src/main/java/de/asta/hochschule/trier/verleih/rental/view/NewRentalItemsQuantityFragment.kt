package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.*
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalItemsQuanityBinding
import de.asta.hochschule.trier.verleih.rental.adapter.RentalItemQuantityAdapter
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
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
		
		setupRecyclerView()
		
		viewModel.objectsLiveData.observe(requireActivity(), { objects ->
			adapter = adapter?.resetData(objects, viewModel.rentalObjectsLiveData.value)
		})
		viewModel.rentalObjectsLiveData.observe(requireActivity(), { rentalObjects ->
			adapter = adapter?.resetData(viewModel.objectsLiveData.value, rentalObjects)
		})
		viewModel.validPagesLiveData.observe(viewLifecycleOwner, {
			if (!it[NewRentalActivity.PAGE_ITEMS_QUANTITY]) {
				Toast.makeText(context, R.string.rental_items_quantity_error, Toast.LENGTH_SHORT)
					.show()
			}
		})
	}
	
	private fun setupRecyclerView() {
		binding.itemsRecyclerview.layoutManager = LinearLayoutManager(context)
		adapter =
			RentalItemQuantityAdapter(
				viewModel.objectsLiveData.value,
				viewModel.rentalObjectsLiveData.value,
				{ obj, position ->
					adapter?.removeObject(obj, position)
					showUndoSnackBar(obj, position)
				}
			) { o, component, quantity ->
				viewModel.updateQuantity(o, component, quantity)
			}
		binding.itemsRecyclerview.adapter = adapter
	}
	
	private fun showUndoSnackBar(obj: RentalObject?, position: Int) {
		val snackBar = Snackbar.make(
			binding.root,
			"${obj?.name} ${getString(R.string.deleted)}",
			Snackbar.LENGTH_LONG
		)
		snackBar.setAction(R.string.undo) {
			adapter?.addObject(obj, position)
		}
		snackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
			override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
				if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
					viewModel.removeRentalObject(obj)
				}
				super.onDismissed(transientBottomBar, event)
			}
		})
		snackBar.show()
	}
	
}