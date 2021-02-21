package de.asta.hochschule.trier.verleih.rental.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.RowItemQuantitySelectBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject

class RentalItemQuantitySelectionAdapter(
	private var objComponents: MutableMap<String, Int>?,
	private var obj: RentalObject?,
	private val updateQuantity: (RentalObject, Pair<String, Int>, Int) -> Unit
) :
	RecyclerView.Adapter<RentalItemQuantitySelectionAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val itemBinding =
			RowItemQuantitySelectBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		
		return ViewHolder(itemBinding, updateQuantity)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val component = objComponents?.toList()?.get(position)
		holder.bind(component, obj)
	}
	
	override fun getItemCount(): Int {
		return objComponents?.size ?: 0
	}
	
	class ViewHolder(
		private val itemBinding: RowItemQuantitySelectBinding,
		private val changeQuantity: (RentalObject, Pair<String, Int>, Int) -> Unit
	) :
		RecyclerView.ViewHolder(itemBinding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(component: Pair<String, Int>?, obj: RentalObject?) {
			itemBinding.itemQuantityDescriptionText.text = "${component?.first}:"
			itemBinding.itemQuantityText.text = component?.second.toString()
			
			if (component != null) {
				val maxQuantity =
					if (component.first == itemView.context.getString(R.string.quantity)) {
						obj?.quantity
					} else {
						obj?.components?.get(component.first)
					}
				
				if (maxQuantity != null) {
					when {
						maxQuantity < 5 -> {
							setupChips(-1, +1, null, null, component, obj, maxQuantity)
						}
						maxQuantity in 6..25 -> {
							setupChips(-1, -5, +5, +1, component, obj, maxQuantity)
						}
						maxQuantity in 26..50 -> {
							setupChips(-5, -10, +10, +5, component, obj, maxQuantity)
						}
						maxQuantity in 51..250 -> {
							setupChips(-10, -50, +50, +10, component, obj, maxQuantity)
						}
						maxQuantity in 251..500 -> {
							setupChips(-50, -100, +100, +50, component, obj, maxQuantity)
						}
						else -> {
							setupChips(-100, -200, +200, +100, component, obj, maxQuantity)
						}
					}
				}
			}
		}
		
		@SuppressLint("SetTextI18n")
		private fun setupChips(
			val1: Int,
			val2: Int,
			val3: Int?,
			val4: Int?,
			component: Pair<String, Int>?,
			obj: RentalObject?,
			maxQuantity: Int?
		) {
			setupChipVisibility(itemBinding.chip3, val3)
			setupChipVisibility(itemBinding.chip4, val4)
			
			itemBinding.chip1.text = "$val1"
			setupChipListener(itemBinding.chip1, val1, component, obj)
			itemBinding.chip2.text = if (val2 > 0) {
				"+$val2"
			} else {
				"$val2"
			}
			setupChipListener(itemBinding.chip2, val2, component, obj)
			if (val3 != null) {
				itemBinding.chip3.text = "+$val3"
				setupChipListener(itemBinding.chip3, val3, component, obj)
			}
			if (val4 != null) {
				itemBinding.chip4.text = "+$val4"
				setupChipListener(itemBinding.chip4, val4, component, obj)
			}
			
			disableChips(val1, val2, val3, val4, maxQuantity, component?.second)
		}
		
		private fun setupChipVisibility(chip: Chip, value: Int?) {
			chip.visibility = if (value == null) {
				View.GONE
			} else {
				View.VISIBLE
			}
		}
		
		private fun disableChips(
			val1: Int,
			val2: Int,
			val3: Int?,
			val4: Int?,
			maximum: Int?,
			quantity: Int?
		) {
			itemBinding.chip1.isEnabled =
				quantity != null && maximum != null && (quantity + val1) in 0..maximum
			itemBinding.chip2.isEnabled =
				quantity != null && maximum != null && (quantity + val2) in 0..maximum
			itemBinding.chip3.isEnabled =
				quantity != null && maximum != null && val3 != null && (quantity + val3) in 0..maximum
			itemBinding.chip4.isEnabled =
				quantity != null && maximum != null && val4 != null && (quantity + val4) in 0..maximum
		}
		
		private fun setupChipListener(
			chip: Chip, value: Int, component: Pair<String, Int>?, obj: RentalObject?
		) {
			chip.setOnClickListener {
				if (component != null && obj != null) {
					changeQuantity(obj, component, component.second.plus(value))
				}
			}
		}
	}
}