package de.asta.hochschule.trier.verleih.rental.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.asta.hochschule.trier.verleih.app.view.TestFragment
import de.asta.hochschule.trier.verleih.rental.view.*

class NewRentalPagerAdapter(private val activity: NewRentalActivity) :
	FragmentStateAdapter(activity) {
	override fun getItemCount(): Int {
		return NewRentalActivity.NUM_PAGES
	}
	
	override fun createFragment(position: Int): Fragment {
		return when (position) {
			0 -> NewRentalDateTimeFragment()
			1 -> NewRentalItemsChoiceFragment()
			2 -> NewRentalItemsQuantityFragment()
			3 -> NewRentalOverviewFragment(activity)
			else -> TestFragment()
		}
	}
	
}