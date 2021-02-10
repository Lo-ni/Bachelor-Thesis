package de.asta.hochschule.trier.verleih.rental.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.asta.hochschule.trier.verleih.app.TestFragment
import de.asta.hochschule.trier.verleih.rental.view.*

class NewRentalPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
	override fun getItemCount(): Int {
		return NewRentalActivity.NUM_PAGES
	}
	
	override fun createFragment(position: Int): Fragment {
		return when (position) {
			0 -> NewRentalDateTimeFragment()
			1 -> NewRentalItemsChoiceFragment()
			2 -> NewRentalItemsQuantityFragment()
			3 -> NewRentalOverviewFragment()
			else -> TestFragment()
		}
	}
	
}