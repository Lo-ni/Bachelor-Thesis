package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.*
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.app.util.*
import de.asta.hochschule.trier.verleih.databinding.RowItemChoiceBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject

class RentalItemChoiceAdapter(
	options: FirebaseRecyclerOptions<RentalObject>,
	private var selectedItems: ArrayList<RentalObject>?,
	private val selectItem: (RentalObject, Boolean) -> Unit,
	private val showBottomSheetDialog: (RentalObject) -> Unit,
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
		loadObjectPicture(holder, model.picture_name)
		if (selectedItems != null) {
			holder.isSelected = selectedItems?.any { it.picture_name == model.picture_name } == true
			setCardBackground(holder)
		}
		holder.itemBinding.itemImageView.setOnClickListener {
			holder.isSelected = !holder.isSelected
			selectItem.invoke(model, holder.isSelected)
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
	
	private fun loadObjectPicture(holder: ViewHolder, pictureName: String?) {
		val path =
			"${Constants.PATH_OBJ_PIC_BIG.childName}$pictureName${Constants.JPG_EXT.childName}"
		val storageRef = FirebaseStorage.getInstance().reference.child(path)
		GlideApp.with(holder.itemView.context).load(storageRef)
			.placeholder(R.drawable.placeholder)
			.into(holder.itemBinding.itemImageView)
	}
	
	private fun setCardBackground(holder: ViewHolder) {
		val colorResId = if (holder.isSelected) {
			R.color.colorPrimary
		} else {
			R.color.onSecondary
		}
		holder.itemBinding.itemChoiceCard.setCardBackgroundColor(
			holder.itemView.context.getColor(colorResId)
		)
	}
	
	class ViewHolder(val itemBinding: RowItemChoiceBinding) :
		RecyclerView.ViewHolder(itemBinding.root) {
		var isSelected: Boolean = false
	}
	
}