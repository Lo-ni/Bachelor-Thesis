package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.RowItemQuantityOverviewBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
import de.asta.hochschule.trier.verleih.util.GlideApp

class RentalItemQuantityAdapter(
	private var objects: ArrayList<RentalObject>?,
	private val removeItem: (RentalObject?, Int) -> Unit
) :
	RecyclerView.Adapter<RentalItemQuantityAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val itemBinding =
			RowItemQuantityOverviewBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		return ViewHolder(itemBinding, removeItem)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(objects?.get(position))
	}
	
	override fun getItemCount(): Int {
		return objects?.size ?: 0
	}
	
	fun removeObject(rentalObject: RentalObject?, position: Int) {
		objects?.remove(rentalObject)
		notifyItemRemoved(position)
	}
	
	fun addObject(rentalObject: RentalObject?, position: Int) {
		if (rentalObject != null) {
			objects?.add(position, rentalObject)
			notifyItemInserted(position)
		}
	}
	
	fun resetObjects(resetObjects: ArrayList<RentalObject>): RentalItemQuantityAdapter {
		objects = resetObjects
		notifyDataSetChanged()
		return this
	}
	
	class ViewHolder(
		private val itemBinding: RowItemQuantityOverviewBinding,
		private val removeItem: (RentalObject?, Int) -> Unit
	) :
		RecyclerView.ViewHolder(itemBinding.root) {
		fun bind(rentalObject: RentalObject?) {
			itemBinding.itemTitle.text = rentalObject?.name
			
			val storageRef =
				FirebaseStorage.getInstance().reference.child("objects/round/${rentalObject?.picture_name}.png")
			GlideApp.with(itemView.context).load(storageRef)
				.placeholder(R.drawable.placeholder)
				.into(itemBinding.itemCircleImageView)
			
			itemBinding.itemDeleteButton.setOnClickListener {
				removeItem.invoke(rentalObject, adapterPosition)
			}
		}
	}
}