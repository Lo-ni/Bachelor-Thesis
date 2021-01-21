package de.asta.hochschule.trier.verleih.event.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.event.viewmodel.EventMainViewModel

class EventMainFragment : Fragment(R.layout.fragment_event_main) {
	
	private val viewModel: EventMainViewModel by lazy {
		ViewModelProvider(this).get(EventMainViewModel::class.java)
	}
	
}