package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.*
import de.asta.hochschule.trier.verleih.databinding.RowRentalListBinding
import de.asta.hochschule.trier.verleih.helper.DateHelper
import de.asta.hochschule.trier.verleih.rental.model.Rental
import org.joda.time.DateTime
import java.util.*

class RentalMainListAdapter(
	private val options: FirebaseRecyclerOptions<Rental>,
	private val showEmptyState: (Boolean) -> Unit
) : FirebaseRecyclerAdapter<
		Rental, RentalMainListAdapter.ViewHolder>(options) {
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ViewHolder {
		val itemBinding =
			RowRentalListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(itemBinding)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Rental) {
		holder.itemBinding.rentalEventName.text = model.eventname
		holder.itemBinding.rentalStatus.text = model.status?.capitalize(Locale.getDefault())
		
		val pickupDateTime = model.pickupdate?.let { DateHelper.getDateTime(it) }
		val returnDateTime = model.returndate?.let { DateHelper.getDateTime(it) }
		if (pickupDateTime?.isAfterNow == true) {
			setupDateTimeText(holder, pickupDateTime)
		} else {
			returnDateTime?.let { setupDateTimeText(holder, it) }
		}
	}
	
	private fun setupDateTimeText(holder: ViewHolder, dateTime: DateTime) {
		holder.itemBinding.rentalTime.text = DateHelper.getTimeString(dateTime)
		holder.itemBinding.rentalDay.text = dateTime.dayOfMonth().asString
		holder.itemBinding.rentalMonth.text = dateTime.monthOfYear().asShortText
	}
	
	override fun onDataChanged() {
		super.onDataChanged()
		showEmptyState.invoke(options.snapshots.size < 1)
	}
	
	class ViewHolder(val itemBinding: RowRentalListBinding) :
		RecyclerView.ViewHolder(itemBinding.root)
	
	companion object {
		private const val TAG = "RentalMainListAdapter"
	}
	
}