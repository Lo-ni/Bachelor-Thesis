package de.asta.hochschule.trier.verleih.rental.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import de.asta.hochschule.trier.verleih.databinding.RowItemQuantitySelectBinding

class RentalItemQuantitySelectionAdapter(
	private var components: Map<String, Int>?,
	private var selectedQuantities: List<Int>?,
	private val changeQuantity: (Int, Int) -> Unit
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
		private val changeQuantity: (Int, Int) -> Unit
	) :
		RecyclerView.ViewHolder(itemBinding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(component: Pair<String, Int>?, quantity: Int?) {
			itemBinding.itemQuantityDescriptionText.text = "${component?.first}:"
			itemBinding.itemQuantityText.text = "$quantity"
			
			if (quantity != null && quantity < 1) {
				itemBinding.chip1.isEnabled = false
				if (component != null && component.second > 5) {
					itemBinding.chip2.isEnabled = false
				}
			}
			
			if (component != null) {
				when {
					component.second < 5 -> {
						setupChips(-1, +1, null, null)
					}
					component.second in 6..25 -> {
						setupChips(-5, -1, +1, +5)
					}
					component.second in 26..50 -> {
						setupChips(-10, -5, +5, +10)
					}
					component.second in 51..250 -> {
						setupChips(-50, -10, +10, +50)
					}
					component.second in 251..500 -> {
						setupChips(-100, -50, +50, +100)
					}
					else -> {
						setupChips(-200, -100, +100, +200)
					}
				}
			}
		}
		
		@SuppressLint("SetTextI18n")
		private fun setupChips(val1: Int, val2: Int, val3: Int?, val4: Int?) {
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
			itemBinding.chip1.setOnClickListener {
				changeQuantity(val1, adapterPosition)
			}
			itemBinding.chip2.text = if (val2 > 0) {
				"+$val2"
			} else {
				"$val2"
			}
			itemBinding.chip2.setOnClickListener {
				changeQuantity(val2, adapterPosition)
			}
			if (val3 != null) {
				itemBinding.chip3.text = "+$val3"
				itemBinding.chip3.setOnClickListener {
					changeQuantity(val3, adapterPosition)
				}
			}
			if (val4 != null) {
				itemBinding.chip4.text = "+$val4"
				itemBinding.chip4.setOnClickListener {
					changeQuantity(val4, adapterPosition)
				}
			}
		}
	}
}