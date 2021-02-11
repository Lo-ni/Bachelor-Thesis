package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.RowItemQuantityOverviewBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject
import de.asta.hochschule.trier.verleih.util.GlideApp

class RentalItemQuantityAdapter(
	private var objects: ArrayList<RentalObject>?,
	private var components: MutableMap<String, MutableMap<String, Int>?>?,
	private val removeItem: (RentalObject?, MutableMap<String, Int>?, Int) -> Unit,
	private val updateQuantity: (RentalObject, Pair<String, Int>, Int, Int) -> Unit
) :
	RecyclerView.Adapter<RentalItemQuantityAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val itemBinding =
			RowItemQuantityOverviewBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		
		return ViewHolder(itemBinding, removeItem, updateQuantity)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val obj = objects?.get(position)
		val objComponents = components?.get(obj?.picture_name)
		holder.bind(obj, objComponents)
	}
	
	override fun getItemCount(): Int {
		return objects?.size ?: 0
	}
	
	fun removeObject(obj: RentalObject?, position: Int) {
		objects?.remove(obj)
		notifyItemRemoved(position)
	}
	
	fun addObject(obj: RentalObject?, position: Int) {
		if (obj != null) {
			objects?.add(position, obj)
			notifyItemInserted(position)
		}
	}
	
	fun resetData(
		objs: ArrayList<RentalObject>?,
		comps: MutableMap<String, MutableMap<String, Int>?>?
	): RentalItemQuantityAdapter {
		objects = objs
		components = comps
		notifyDataSetChanged()
		return this
	}
	
	class ViewHolder(
		private val itemBinding: RowItemQuantityOverviewBinding,
		private val removeItem: (RentalObject?, MutableMap<String, Int>?, Int) -> Unit,
		private val updateQuantity: (RentalObject, Pair<String, Int>, Int, Int) -> Unit
	) :
		RecyclerView.ViewHolder(itemBinding.root) {
		
		private lateinit var adapter: RentalItemQuantitySelectionAdapter
		
		fun bind(obj: RentalObject?, objComponents: MutableMap<String, Int>?) {
			itemBinding.itemTitle.text = obj?.name
			
			val storageRef =
				FirebaseStorage.getInstance().reference.child("objects/round/${obj?.picture_name}.png")
			GlideApp.with(itemView.context).load(storageRef)
				.placeholder(R.drawable.placeholder)
				.into(itemBinding.itemCircleImageView)
			
			itemBinding.itemDeleteButton.setOnClickListener {
				removeItem.invoke(obj, objComponents, adapterPosition)
			}
			
			itemBinding.itemQuantityRecyclerView.layoutManager =
				LinearLayoutManager(itemView.context)
			adapter = RentalItemQuantitySelectionAdapter(objComponents, obj, updateQuantity)
			itemBinding.itemQuantityRecyclerView.adapter = adapter
		}
	}
	
	companion object {
		private const val TAG = "RentalItemQuantityAdapter"
	}
}