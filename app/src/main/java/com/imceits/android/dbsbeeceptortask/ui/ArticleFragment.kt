package com.imceits.android.dbsbeeceptortask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.imceits.android.dbsbeeceptortask.R
import com.imceits.android.dbsbeeceptortask.data.AbsentLiveData
import com.imceits.android.dbsbeeceptortask.data.AutoClearedValue
import com.imceits.android.dbsbeeceptortask.databinding.FragmentArticleBinding
import com.imceits.android.dbsbeeceptortask.di.Injectable
import javax.inject.Inject

class ArticleFragment : Fragment(), Injectable {

    private lateinit var articleViewModel: ArticleViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var binding by AutoClearedValue<FragmentArticleBinding>(this)
    lateinit var adapter: ArticleAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = this
        binding.articleList.setHasFixedSize(true)
        binding.articleList.isNestedScrollingEnabled = false
        binding.articleList.itemAnimator = DefaultItemAnimator()
        adapter = ArticleAdapter()
        binding.articleList.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleViewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewModel::class.java)
        binding.viewModel = articleViewModel
        articleViewModel.fetchArticles().observe(viewLifecycleOwner, Observer {
            binding.resource = it
            it.data?.let { data ->
                adapter.setData(data)
            }
        })
    }
}
