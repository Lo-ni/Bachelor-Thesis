package de.asta.hochschule.trier.verleih.rental.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import de.asta.hochschule.trier.verleih.databinding.ActivityNewRentalBinding
import de.asta.hochschule.trier.verleih.rental.adapter.NewRentalPagerAdapter

class NewRentalActivity : FragmentActivity() {
	
	private lateinit var binding: ActivityNewRentalBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityNewRentalBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		binding.appbar.setNavigationOnClickListener {
			onBackPressed()
		}
		
		val pagerAdapter = NewRentalPagerAdapter(this)
		binding.newRentalPager.adapter = pagerAdapter
		
		TabLayoutMediator(binding.newRentalPagerTabs, binding.newRentalPager) { tab, position ->
		
		}.attach()
		
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
			}
			
		})
		
		binding.pagerBackButton.setOnClickListener {
			onBackPressed()
		}
		
		binding.pagerNextButton.setOnClickListener {
		
		}
		
	}
	
	override fun onBackPressed() {
		if (binding.newRentalPager.currentItem == 0) {
			super.onBackPressed()
		} else {
			binding.newRentalPager.currentItem = binding.newRentalPager.currentItem - 1
		}
	}
	
	companion object {
		private const val TAG = "NewRentalActivity"
		const val NUM_PAGES = 4
	}
	
}