package de.asta.hochschule.trier.verleih.admin.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.admin.viewmodel.AdminMainViewModel

class AdminMainFragment : Fragment(R.layout.fragment_admin_main) {
	
	private val viewModel: AdminMainViewModel by lazy {
		ViewModelProvider(this).get(AdminMainViewModel::class.java)
	}
	
}