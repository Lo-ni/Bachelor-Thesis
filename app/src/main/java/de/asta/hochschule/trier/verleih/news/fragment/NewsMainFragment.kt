package de.asta.hochschule.trier.verleih.news.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.asta.hochschule.trier.verleih.R
import de.asta.hochschule.trier.verleih.news.viewmodel.NewsMainViewModel

class NewsMainFragment : Fragment(R.layout.fragment_news_main) {
	
	private val viewModel: NewsMainViewModel by lazy {
		ViewModelProvider(this).get(NewsMainViewModel::class.java)
	}
	
}