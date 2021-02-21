package de.asta.hochschule.trier.verleih.rental.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.google.firebase.storage.FirebaseStorage
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.app.util.*
import de.asta.hochschule.trier.verleih.databinding.RowItemQuantityOverviewBinding
import de.asta.hochschule.trier.verleih.rental.model.RentalObject

class RentalItemQuantityAdapter(
	private var objects: ArrayList<RentalObject>?,
	private var components: MutableMap<String, MutableMap<String, Int>?>?,
	private val removeItem: (RentalObject?, Int) -> Unit,
	private val updateQuantity: (RentalObject, Pair<String, Int>, Int) -> Unit
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
		objects: ArrayList<RentalObject>?,
		components: MutableMap<String, MutableMap<String, Int>?>?
	): RentalItemQuantityAdapter {
		this.objects = objects
		this.components = components
		notifyDataSetChanged()
		return this
	}
	
	class ViewHolder(
		private val itemBinding: RowItemQuantityOverviewBinding,
		private val removeItem: (RentalObject?, Int) -> Unit,
		private val updateQuantity: (RentalObject, Pair<String, Int>, Int) -> Unit
	) :
		RecyclerView.ViewHolder(itemBinding.root) {
		
		private lateinit var adapter: RentalItemQuantitySelectionAdapter
		
		fun bind(obj: RentalObject?, objComponents: MutableMap<String, Int>?) {
			itemBinding.itemTitle.text = obj?.name
			
			loadObjectPicture(obj?.picture_name)
			setupRecyclerView(obj, objComponents)
			
			itemBinding.itemDeleteButton.setOnClickListener {
				removeItem.invoke(obj, adapterPosition)
			}
		}
		
		private fun loadObjectPicture(pictureName: String?) {
			val path =
				"${Constants.PATH_OBJ_PIC_ROUND.childName}$pictureName${Constants.PNG_EXT.childName}"
			val storageRef = FirebaseStorage.getInstance().reference.child(path)
			GlideApp.with(itemView.context).load(storageRef)
				.placeholder(R.drawable.placeholder)
				.into(itemBinding.itemCircleImageView)
		}
		
		private fun setupRecyclerView(obj: RentalObject?, objComponents: MutableMap<String, Int>?) {
			itemBinding.itemQuantityRecyclerView.layoutManager =
				LinearLayoutManager(itemView.context)
			adapter = RentalItemQuantitySelectionAdapter(objComponents, obj, updateQuantity)
			itemBinding.itemQuantityRecyclerView.adapter = adapter
		}
	}
	
}