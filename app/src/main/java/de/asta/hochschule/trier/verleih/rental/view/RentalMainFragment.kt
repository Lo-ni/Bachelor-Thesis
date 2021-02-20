package de.asta.hochschule.trier.verleih.rental.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.firebase.ui.database.*
import com.google.firebase.database.FirebaseDatabase
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentRentalMainBinding
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.adapter.RentalMainListAdapter
import de.asta.hochschule.trier.verleih.rental.model.Rental
import org.joda.time.DateTime

class RentalMainFragment : Fragment(R.layout.fragment_rental_main) {
	
	private lateinit var binding: FragmentRentalMainBinding
	
	private lateinit var recentRentalsAdapter: RentalMainListAdapter
	private lateinit var pastRentalsAdapter: RentalMainListAdapter
	
	private var recentRentalsIsEmpty = false
	private var pastRentalsIsEmpty = false
	private var recentRentalsIsExpanded = true
	private var pastRentalsIsExpanded = true
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentRentalMainBinding.inflate(layoutInflater)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		binding.rentalsFab.setOnClickListener {
			val intent = Intent(context, NewRentalActivity::class.java)
			startActivity(intent)
		}
		
		val parser = SnapshotParser { snapshot ->
			val rental = snapshot.getValue(Rental::class.java)
			if (rental != null) {
				rental.id = snapshot.key
				return@SnapshotParser rental
			}
			return@SnapshotParser Rental()
		}
		
		val queryRecent =
			FirebaseDatabase.getInstance().reference.child("rentals").orderByChild("returndate")
				.startAt(DateTime.now().toString(DateHelper.TIMESTAMP_FORMAT))
		val optionsRecent =
			FirebaseRecyclerOptions.Builder<Rental>().setQuery(queryRecent, parser).build()
		recentRentalsAdapter =
			RentalMainListAdapter(requireActivity(), optionsRecent) { showEmptyState ->
				recentRentalsIsEmpty = showEmptyState
				toggleEmptyState(
					recentRentalsIsEmpty,
					binding.noRecentRentalsText,
					recentRentalsIsExpanded,
					binding.recentRentalsHeaderIcon
				)
			}
		binding.recentRentalsRecyclerView.layoutManager = LinearLayoutManager(this.context)
		binding.recentRentalsRecyclerView.adapter = recentRentalsAdapter
		
		val queryPast =
			FirebaseDatabase.getInstance().reference.child("rentals").orderByChild("returndate")
				.endAt(DateTime.now().toString(DateHelper.TIMESTAMP_FORMAT))
		val optionsPast =
			FirebaseRecyclerOptions.Builder<Rental>().setQuery(queryPast, parser)
				.build()
		pastRentalsAdapter =
			RentalMainListAdapter(requireActivity(), optionsPast) { showEmptyState ->
				pastRentalsIsEmpty = showEmptyState
				toggleEmptyState(
					pastRentalsIsEmpty,
					binding.noPastRentalsText,
					pastRentalsIsExpanded,
					binding.pastRentalsHeaderIcon
				)
			}
		binding.pastRentalsRecyclerView.layoutManager = LinearLayoutManager(this.context)
		binding.pastRentalsRecyclerView.adapter = pastRentalsAdapter
		
		binding.recentRentalsHeaderLayout.setOnClickListener {
			recentRentalsIsExpanded = !recentRentalsIsExpanded
			toggleExpandCollapseState(
				recentRentalsIsExpanded,
				binding.recentRentalsHeaderIcon,
				binding.recentRentalsRecyclerView,
				recentRentalsIsEmpty,
				binding.noRecentRentalsText
			)
		}
		binding.pastRentalsHeaderLayout.setOnClickListener {
			pastRentalsIsExpanded = !pastRentalsIsExpanded
			toggleExpandCollapseState(
				pastRentalsIsExpanded,
				binding.pastRentalsHeaderIcon,
				binding.pastRentalsRecyclerView,
				pastRentalsIsEmpty,
				binding.noPastRentalsText
			)
		}
	}
	
	private fun toggleEmptyState(
		showEmptyState: Boolean,
		emptyStateTextView: TextView,
		isExpanded: Boolean,
		icon: ImageView
	) {
		emptyStateTextView.visibility = if (showEmptyState) {
			View.VISIBLE
		} else {
			View.GONE
		}
		if (isExpanded) {
			icon.setImageResource(R.drawable.ic_chevron_up)
		} else {
			icon.setImageResource(R.drawable.ic_chevron_down)
		}
	}
	
	private fun toggleExpandCollapseState(
		isExpanded: Boolean,
		icon: ImageView,
		recyclerView: RecyclerView,
		showEmptyState: Boolean,
		emptyStateTextView: TextView
	) {
		if (isExpanded) {
			icon.setImageResource(R.drawable.ic_chevron_up)
		} else {
			icon.setImageResource(R.drawable.ic_chevron_down)
		}
		
		recyclerView.visibility = if (isExpanded && !showEmptyState) {
			View.VISIBLE
		} else {
			View.GONE
		}
		emptyStateTextView.visibility = if (isExpanded && showEmptyState) {
			View.VISIBLE
		} else {
			View.GONE
		}
	}
	
	override fun onStart() {
		super.onStart()
		recentRentalsAdapter.startListening()
		pastRentalsAdapter.startListening()
	}
	
	override fun onStop() {
		super.onStop()
		recentRentalsAdapter.stopListening()
		pastRentalsAdapter.stopListening()
	}
	
	companion object {
		private const val TAG = "RentalMainFragment"
	}
	
}