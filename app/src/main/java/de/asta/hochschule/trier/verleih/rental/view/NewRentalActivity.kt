package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.ActivityNewRentalBinding
import de.asta.hochschule.trier.verleih.rental.adapter.NewRentalPagerAdapter
import de.asta.hochschule.trier.verleih.rental.model.*
import de.asta.hochschule.trier.verleih.rental.viewmodel.NewRentalViewModel

class NewRentalActivity : FragmentActivity() {
	
	private lateinit var binding: ActivityNewRentalBinding
	private val viewModel: NewRentalViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityNewRentalBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		binding.appbar.setNavigationOnClickListener {
			onBackPressed()
		}
		
		setupEditRental()
		setupViewPager()
	}
	
	override fun onBackPressed() {
		if (binding.newRentalPager.currentItem == 0) {
			super.onBackPressed()
		} else {
			binding.newRentalPager.currentItem = binding.newRentalPager.currentItem - 1
		}
	}
	
	fun goToPage(page: Int) {
		if (page in PAGE_DATE_TIME..PAGE_OVERVIEW) {
			binding.newRentalPager.currentItem = page
		}
	}
	
	private fun setupViewPager() {
		binding.newRentalPager.adapter = NewRentalPagerAdapter(this)
		TabLayoutMediator(binding.newRentalPagerTabs, binding.newRentalPager) { _, _ -> }.attach()
		binding.newRentalPager.isUserInputEnabled = false
		binding.newRentalPager.registerOnPageChangeCallback(object :
			ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				if (position == PAGE_OVERVIEW) {
					binding.pagerNextButton.setImageResource(R.drawable.ic_check)
				} else {
					binding.pagerNextButton.setImageResource(R.drawable.ic_chevron_right)
				}
			}
		})
		binding.pagerBackButton.setOnClickListener {
			onBackPressed()
		}
		binding.pagerNextButton.setOnClickListener {
			if (viewModel.validateInput(binding.newRentalPager.currentItem)) {
				if (binding.newRentalPager.currentItem == PAGE_OVERVIEW) {
					viewModel.saveRentalToDatabase()
					finish()
				} else {
					binding.newRentalPager.currentItem = binding.newRentalPager.currentItem + 1
				}
			}
		}
	}
	
	private fun setupEditRental() {
		val rental = Gson().fromJson(
			intent.getStringExtra(EditRentalActivity.INTENT_EXTRA_RENTAL),
			Rental::class.java
		)
		val objectsType = object : TypeToken<ArrayList<RentalObject>>() {}.type
		val objects = Gson().fromJson<ArrayList<RentalObject>>(
			intent.getStringExtra(EditRentalActivity.INTENT_EXTRA_RENTAL_OBJECTS),
			objectsType
		)
		if (rental != null && objects != null) {
			viewModel.setupViewModel(rental, objects)
			binding.appbar.setTitle(R.string.edit_rental)
		}
	}
	
	companion object {
		const val NUM_PAGES = 4
		const val PAGE_DATE_TIME = 0
		const val PAGE_ITEMS_CHOICE = 1
		const val PAGE_ITEMS_QUANTITY = 2
		const val PAGE_OVERVIEW = 3
	}
	
}