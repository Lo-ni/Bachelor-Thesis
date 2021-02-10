package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.databinding.ActivityNewRentalBinding
import de.asta.hochschule.trier.verleih.rental.adapter.NewRentalPagerAdapter
import de.asta.hochschule.trier.verleih.rental.model.Rental
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
		
		val pagerAdapter = NewRentalPagerAdapter(this)
		binding.newRentalPager.adapter = pagerAdapter
		TabLayoutMediator(binding.newRentalPagerTabs, binding.newRentalPager) { _, _ -> }.attach()
		
		binding.newRentalPager.isUserInputEnabled = false
		binding.newRentalPager.registerOnPageChangeCallback(object :
			ViewPager2.OnPageChangeCallback() {
			
			override fun onPageScrollStateChanged(state: Int) {
				Log.d(TAG, "page scroll state changed $state")
			}
			
			override fun onPageScrolled(
				position: Int,
				positionOffset: Float,
				positionOffsetPixels: Int
			) {
				Log.d(TAG, "page scrolled $position")
				
			}
			
			override fun onPageSelected(position: Int) {
				Log.d(TAG, "page selected $position")
				if (position == PAGE_OVERVIEW) {
					binding.pagerNextButton.setImageResource(R.drawable.ic_check)
				}
			}
		})
		
		binding.pagerBackButton.setOnClickListener {
			onBackPressed()
		}
		
		binding.pagerNextButton.setOnClickListener {
			if (!isValidInput(binding.newRentalPager.currentItem, viewModel.rentalLiveData.value)) {
				Toast.makeText(this, "Input invalid", Toast.LENGTH_SHORT).show()
			} else {
				if (binding.newRentalPager.currentItem == PAGE_OVERVIEW) {
					Log.d(TAG, "save")
				} else {
					binding.newRentalPager.currentItem = binding.newRentalPager.currentItem + 1
				}
			}
		}
		
		viewModel.rentalLiveData.observe(this, { rental ->
			if (isValidInput(binding.newRentalPager.currentItem, rental)) {
				Log.d(TAG, "Valid input")
			}
		})
		
	}
	
	override fun onBackPressed() {
		if (binding.newRentalPager.currentItem == 0) {
			super.onBackPressed()
		} else {
			binding.newRentalPager.currentItem = binding.newRentalPager.currentItem - 1
		}
	}
	
	private fun isValidInput(page: Int, rental: Rental?): Boolean {
		when (page) {
			PAGE_DATE_TIME -> {
				if (rental?.eventname == null || rental.pickupdate == null || rental.returndate == null) {
					return false
				}
				return true
			}
			PAGE_ITEMS_CHOICE -> {
				if (rental?.objects == null) {
					return false
				}
				return true
			}
			PAGE_ITEMS_QUANTITY -> {
				if (rental?.objects == null) {
					return false
				}
				return true
			}
			else -> return true
		}
	}
	
	companion object {
		private const val TAG = "NewRentalActivity"
		const val NUM_PAGES = 4
		
		private const val PAGE_DATE_TIME = 0
		private const val PAGE_ITEMS_CHOICE = 1
		private const val PAGE_ITEMS_QUANTITY = 2
		private const val PAGE_OVERVIEW = 3
	}
	
}