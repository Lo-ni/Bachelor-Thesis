package de.asta.hochschule.trier.verleih.settings.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.settings.viewmodel.SettingsMainViewModel

class SettingsMainFragment : Fragment(R.layout.fragment_settings_main) {
	
	private val viewModel: SettingsMainViewModel by lazy {
		ViewModelProvider(this).get(SettingsMainViewModel::class.java)
	}
	
}