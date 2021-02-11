package de.asta.hochschule.trier.verleih.rental.adapter

import android.annotation.SuppressLint
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.RowItemOverviewBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
import de.asta.hochschule.trier.verleih.util.GlideApp

class RentalItemOverviewAdapter(
	private var objects: ArrayList<RentalObject>?,
	private var components: MutableMap<String, MutableMap<String, Int>>?
) :
	RecyclerView.Adapter<RentalItemOverviewAdapter.ViewHolder>() {
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ViewHolder {
		val itemBinding =
			RowItemOverviewBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		
		return ViewHolder(itemBinding)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val obj = objects?.get(position)
		val objComponents = components?.get(obj?.picture_name)
		holder.bind(obj, objComponents)
	}
	
	override fun getItemCount(): Int {
		return objects?.size ?: 0
	}
	
	fun resetData(
		objs: ArrayList<RentalObject>?,
		comps: MutableMap<String, MutableMap<String, Int>>?
	): RentalItemOverviewAdapter {
		objects = objs
		components = comps
		notifyDataSetChanged()
		return this
	}
	
	class ViewHolder(private val itemBinding: RowItemOverviewBinding) :
		RecyclerView.ViewHolder(itemBinding.root) {
		
		@SuppressLint("SetTextI18n")
		fun bind(obj: RentalObject?, objComponents: MutableMap<String, Int>?) {
			objComponents?.get(obj?.picture_name)
			val itemTitle = if (objComponents != null && objComponents.size > 1) {
				obj?.name
			} else {
				"${objComponents?.toList()?.get(0)?.second}x ${obj?.name}"
			}
			itemBinding.itemTitle.text = itemTitle
			
			if (objComponents != null && objComponents.size > 1) {
				itemBinding.itemComponentsLayout.removeAllViews()
				objComponents.forEach {
					val textView = TextView(itemView.context)
					textView.text = "${it.value}x ${it.key}"
					itemBinding.itemComponentsLayout.addView(textView)
				}
			}
			
			val storageRef =
				FirebaseStorage.getInstance().reference.child("objects/round/${obj?.picture_name}.png")
			GlideApp.with(itemView.context).load(storageRef)
				.placeholder(R.drawable.placeholder)
				.into(itemBinding.itemCircleImageView)
		}
		
	}
}