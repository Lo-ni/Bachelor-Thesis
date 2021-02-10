package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.*
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.RowItemChoiceBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
import de.asta.hochschule.trier.verleih.util.GlideApp
import java.util.*

class RentalItemChoiceAdapter(
	options: FirebaseRecyclerOptions<RentalObject>
) : FirebaseRecyclerAdapter<
		RentalObject, RentalItemChoiceAdapter.ViewHolder>(options) {
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ViewHolder {
		val itemBinding =
			RowItemChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(itemBinding)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RentalObject) {
		holder.itemBinding.itemNameText.text = model.name?.capitalize(Locale.getDefault())
		
		val storageRef =
			FirebaseStorage.getInstance().reference.child("objects/big/${model.picture_name}.jpg")
		GlideApp.with(holder.itemView.context).load(storageRef)
			.placeholder(R.drawable.placeholder)
			.into(holder.itemBinding.itemImageView)
	}
	
	class ViewHolder(val itemBinding: RowItemChoiceBinding) :
		RecyclerView.ViewHolder(itemBinding.root)
	
	companion object {
		private const val TAG = "RentalMainListAdapter"
	}
}