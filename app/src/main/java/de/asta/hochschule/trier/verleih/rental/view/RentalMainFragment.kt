package de.asta.hochschule.trier.verleih.rental.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.firebase.ui.database.*
import com.google.firebase.database.*
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.FragmentRentalMainBinding
import de.asta.hochschule.trier.verleih.rental.adapter.RentalMainListAdapter
import de.asta.hochschule.trier.verleih.rental.model.Rental
import de.asta.hochschule.trier.verleih.util.*
import org.joda.time.DateTime

class RentalMainFragment : Fragment(R.layout.fragment_rental_main) {
	
	private lateinit var binding: FragmentRentalMainBinding
	private var recentRentalsIsExpanded = true
	private var pastRentalsIsExpanded = true
	private var recentRentalsIsEmpty = false
	private var pastRentalsIsEmpty = false
	
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
		
		val query = FirebaseDatabase.getInstance().reference.child(Constants.RENTALS.childName)
			.orderByChild(Constants.RETURN_DATE.childName)
		val queryRecent = query.startAt(DateTime.now().toString(DateHelper.TIMESTAMP_FORMAT))
		val queryPast = query.endAt(DateTime.now().toString(DateHelper.TIMESTAMP_FORMAT))
		setupRecyclerView(
			binding.recentRentalsRecyclerView,
			queryRecent,
			binding.noRecentRentalsText,
			binding.recentRentalsHeaderIcon,
			binding.recentRentalsHeaderLayout,
			true
		)
		setupRecyclerView(
			binding.pastRentalsRecyclerView,
			queryPast,
			binding.noPastRentalsText,
			binding.pastRentalsHeaderIcon,
			binding.pastRentalsHeaderLayout,
			false
		)
	}
	
	override fun onStart() {
		super.onStart()
		(binding.recentRentalsRecyclerView.adapter as RentalMainListAdapter).startListening()
		(binding.pastRentalsRecyclerView.adapter as RentalMainListAdapter).startListening()
	}
	
	override fun onStop() {
		super.onStop()
		(binding.recentRentalsRecyclerView.adapter as RentalMainListAdapter).stopListening()
		(binding.pastRentalsRecyclerView.adapter as RentalMainListAdapter).stopListening()
	}
	
	private fun setupRecyclerView(
		recyclerView: RecyclerView,
		query: Query,
		noRentalsTextView: TextView,
		headerIcon: ImageView,
		headerLayout: ConstraintLayout,
		isRecentRental: Boolean
	) {
		val parser = SnapshotParser { snapshot ->
			val rental = snapshot.getValue(Rental::class.java)
			if (rental != null) {
				rental.id = snapshot.key
				return@SnapshotParser rental
			}
			return@SnapshotParser Rental()
		}
		val options = FirebaseRecyclerOptions.Builder<Rental>().setQuery(query, parser).build()
		recyclerView.layoutManager = LinearLayoutManager(this.context)
		recyclerView.adapter = RentalMainListAdapter(requireActivity(), options) { showEmptyState ->
			val expanded = if (isRecentRental) {
				recentRentalsIsEmpty = showEmptyState
				recentRentalsIsExpanded
			} else {
				pastRentalsIsEmpty = showEmptyState
				pastRentalsIsExpanded
			}
			setVisibility(noRentalsTextView, showEmptyState)
			setIcon(headerIcon, expanded)
		}
		
		headerLayout.setOnClickListener {
			val expanded = if (isRecentRental) {
				recentRentalsIsExpanded = !recentRentalsIsExpanded
				recentRentalsIsExpanded
			} else {
				pastRentalsIsExpanded = !pastRentalsIsExpanded
				pastRentalsIsExpanded
			}
			val isEmpty = if (isRecentRental) {
				recentRentalsIsEmpty
			} else {
				pastRentalsIsEmpty
			}
			setIcon(headerIcon, expanded)
			setVisibility(recyclerView, expanded)
			setVisibility(noRentalsTextView, expanded && isEmpty)
		}
	}
	
	private fun setVisibility(view: View, show: Boolean) {
		view.visibility = if (show) {
			View.VISIBLE
		} else {
			View.GONE
		}
	}
	
	private fun setIcon(icon: ImageView, isExpanded: Boolean) {
		if (isExpanded) {
			icon.setImageResource(R.drawable.ic_chevron_up)
		} else {
			icon.setImageResource(R.drawable.ic_chevron_down)
		}
	}
	
}