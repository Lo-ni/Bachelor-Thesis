package de.asta.hochschule.trier.verleih.rental.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import de.asta.hochschule.trier.verleih.databinding.RowItemQuantitySelectBinding

class RentalItemQuantitySelectionAdapter(
	private var components: Map<String, Int>?,
	private var selectedQuantities: List<Int>?,
	private val changeQuantity: (Pair<String, Int>, Int, Int) -> Unit
) :
	RecyclerView.Adapter<RentalItemQuantitySelectionAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val itemBinding =
			RowItemQuantitySelectBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		return ViewHolder(itemBinding, changeQuantity)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(components?.toList()?.get(position), selectedQuantities?.get(position))
	}
	
	override fun getItemCount(): Int {
		return components?.size ?: 0
	}
	
	class ViewHolder(
		private val itemBinding: RowItemQuantitySelectBinding,
		private val changeQuantity: (Pair<String, Int>, Int, Int) -> Unit
	) :
		RecyclerView.ViewHolder(itemBinding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(component: Pair<String, Int>?, quantity: Int?) {
			itemBinding.itemQuantityDescriptionText.text = "${component?.first}:"
			itemBinding.itemQuantityText.text = "$quantity"
			
			if (component != null) {
				when {
					component.second < 5 -> {
						setupChips(-1, +1, null, null, component, quantity)
					}
					component.second in 6..25 -> {
						setupChips(-5, -1, +1, +5, component, quantity)
					}
					component.second in 26..50 -> {
						setupChips(-10, -5, +5, +10, component, quantity)
					}
					component.second in 51..250 -> {
						setupChips(-50, -10, +10, +50, component, quantity)
					}
					component.second in 251..500 -> {
						setupChips(-100, -50, +50, +100, component, quantity)
					}
					else -> {
						setupChips(-200, -100, +100, +200, component, quantity)
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
			quantity: Int?
		) {
			itemBinding.chip3.visibility = if (val3 == null) {
				View.GONE
			} else {
				View.VISIBLE
			}
			itemBinding.chip4.visibility = if (val4 == null) {
				View.GONE
			} else {
				View.VISIBLE
			}
			
			itemBinding.chip1.text = "$val1"
			setupChipListener(itemBinding.chip1, val1, component, quantity)
			itemBinding.chip2.text = if (val2 > 0) {
				"+$val2"
			} else {
				"$val2"
			}
			setupChipListener(itemBinding.chip2, val2, component, quantity)
			if (val3 != null) {
				itemBinding.chip3.text = "+$val3"
				setupChipListener(itemBinding.chip3, val3, component, quantity)
			}
			if (val4 != null) {
				itemBinding.chip4.text = "+$val4"
				setupChipListener(itemBinding.chip4, val4, component, quantity)
			}
			
			disableChips(val1, val2, val3, val4, component?.second, quantity)
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
			chip: Chip, value: Int, component: Pair<String, Int>?, quantity: Int?
		) {
			chip.setOnClickListener {
				if (component != null && quantity != null) {
					changeQuantity(component, quantity.plus(value), adapterPosition)
				}
			}
		}
	}
}