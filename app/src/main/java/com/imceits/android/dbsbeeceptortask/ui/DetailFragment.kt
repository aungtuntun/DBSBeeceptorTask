package com.imceits.android.dbsbeeceptortask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.imceits.android.dbsbeeceptortask.data.AutoClearedValue
import com.imceits.android.dbsbeeceptortask.databinding.FragmentDetailBinding
import com.imceits.android.dbsbeeceptortask.di.Injectable
import javax.inject.Inject

class DetailFragment : Fragment(), Injectable {

    private lateinit var detailViewModel: DetailViewModel
    private var articleId = 0
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var binding by AutoClearedValue<FragmentDetailBinding>(this)
    private val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        articleId = args.articleId
        binding.lifecycleOwner = this
        val directions = DetailFragmentDirections.actionNavDetailToNavUpdate()
        directions.arcId = articleId
        binding.fabEdit.setOnClickListener(Navigation.createNavigateOnClickListener(directions))
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailViewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        detailViewModel.updateParam(articleId)
        detailViewModel.article.observe(viewLifecycleOwner, Observer {
            binding.data = it
        })
    }
}
