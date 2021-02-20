package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import de.asta.hochschule.trier.verleih.databinding.RowNoteOverviewBinding
import de.asta.hochschule.trier.verleih.helper.DateHelper

class EditRentalNotesAdapter(private var notes: MutableMap<String, String>?) :
	RecyclerView.Adapter<EditRentalNotesAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val itemBinding =
			RowNoteOverviewBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		
		return ViewHolder(itemBinding)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(notes?.toList()?.get(position))
	}
	
	override fun getItemCount(): Int {
		return notes?.size ?: 0
	}
	
	class ViewHolder(private val itemBinding: RowNoteOverviewBinding) :
		RecyclerView.ViewHolder(itemBinding.root) {
		
		fun bind(note: Pair<String, String>?) {
			itemBinding.noteTimeText.text =
				note?.first?.toLong()?.let {
					DateHelper.timeStampToString(it, DateHelper.DATE_TIME_FORMAT)
				}
			itemBinding.noteContentText.text = note?.second
		}
	}
}