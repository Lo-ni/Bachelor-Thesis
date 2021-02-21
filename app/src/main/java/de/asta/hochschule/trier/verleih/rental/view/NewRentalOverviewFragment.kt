package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentNewRentalOverviewBinding
import de.asta.hochschule.trier.verleih.rental.adapter.RentalItemOverviewAdapter
import de.asta.hochschule.trier.verleih.rental.model.Rental
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel
import de.asta.hochschule.trier.verleih.util.DateHelper

class NewRentalOverviewFragment(private val parentActivity: NewRentalActivity) :
	Fragment(R.layout.fragment_new_rental_overview) {
	
	private lateinit var binding: FragmentNewRentalOverviewBinding
	private val viewModel: NewRentalViewModel by activityViewModels()
	private var adapter: RentalItemOverviewAdapter? = null
	
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
		
		updateTextViews(viewModel.rentalLiveData.value)
		setupRecyclerView()
		
		binding.editInformationButton.setOnClickListener {
			parentActivity.goToPage(NewRentalActivity.PAGE_DATE_TIME)
		}
		binding.editItemsButton.setOnClickListener {
			parentActivity.goToPage(NewRentalActivity.PAGE_ITEMS_QUANTITY)
		}
		binding.noteTextInputEditText.doAfterTextChanged {
			viewModel.enterEventNote(it.toString())
		}
		
		viewModel.objectsLiveData.observe(requireActivity(), { objects ->
			adapter = adapter?.resetData(objects, viewModel.rentalObjectsLiveData.value)
		})
		viewModel.rentalObjectsLiveData.observe(requireActivity(), { rentalObjects ->
			adapter = adapter?.resetData(viewModel.objectsLiveData.value, rentalObjects)
		})
		viewModel.rentalLiveData.observe(requireActivity(), { rental ->
			updateTextViews(rental)
		})
	}
	
	private fun setupRecyclerView() {
		binding.itemsRecyclerview.layoutManager = LinearLayoutManager(context)
		adapter = RentalItemOverviewAdapter(
			viewModel.objectsLiveData.value,
			viewModel.rentalObjectsLiveData.value
		)
		binding.itemsRecyclerview.adapter = adapter
	}
	
	private fun updateTextViews(rental: Rental?) {
		binding.eventTitleText.text = rental?.eventname
		binding.eventPickupText.text =
			rental?.pickupdate?.let {
				DateHelper.getDateTime(it).toString(DateHelper.LONG_DATE_TIME_FORMAT)
			}
		binding.eventReturnText.text =
			rental?.returndate?.let {
				DateHelper.getDateTime(it).toString(DateHelper.LONG_DATE_TIME_FORMAT)
			}
	}
	
}