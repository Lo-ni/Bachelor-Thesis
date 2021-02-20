package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.*
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.RowItemChoiceBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
import de.asta.hochschule.trier.verleih.util.GlideApp

class RentalItemChoiceAdapter(
	options: FirebaseRecyclerOptions<RentalObject>,
	private val showBottomSheetDialog: (RentalObject) -> Unit,
	private val selectItem: (RentalObject, Boolean) -> Unit,
	private var selectedItems: ArrayList<RentalObject>?
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
		holder.itemBinding.itemNameText.text = model.name
		
		val size = (holder.itemView.context.resources.displayMetrics.widthPixels / 3) - (4 * 8)
		holder.itemBinding.itemImageView.layoutParams = ConstraintLayout.LayoutParams(size, size)
		
		val storageRef =
			FirebaseStorage.getInstance().reference.child("objects/big/${model.picture_name}.jpg")
		GlideApp.with(holder.itemView.context).load(storageRef)
			.placeholder(R.drawable.placeholder)
			.into(holder.itemBinding.itemImageView)
		
		selectedItems?.forEach {
			if (it.picture_name == model.picture_name) {
				holder.isSelected = true
				return@forEach
			}
		}
		var colorResId = if (holder.isSelected) {
			R.color.colorSecondaryLight
		} else {
			R.color.surface
		}
		holder.itemBinding.itemChoiceCard.setCardBackgroundColor(
			holder.itemView.context.getColor(colorResId)
		)
		
		holder.itemBinding.itemImageView.setOnClickListener {
			holder.isSelected = !holder.isSelected
			selectItem.invoke(model, holder.isSelected)
			
			colorResId = if (holder.isSelected) {
				R.color.colorSecondaryLight
			} else {
				R.color.surface
				
			}
			holder.itemBinding.itemChoiceCard.setCardBackgroundColor(
				holder.itemView.context.getColor(colorResId)
			)
		}
		
		holder.itemBinding.itemInformationButton.setOnClickListener {
			showBottomSheetDialog.invoke(model)
		}
	}
	
	fun resetSelectedItems(items: ArrayList<RentalObject>): RentalItemChoiceAdapter {
		selectedItems = items
		notifyDataSetChanged()
		return this
	}
	
	class ViewHolder(val itemBinding: RowItemChoiceBinding) :
		RecyclerView.ViewHolder(itemBinding.root) {
		var isSelected: Boolean = false
	}
	
	companion object {
		private const val TAG = "RentalMainListAdapter"
	}
}