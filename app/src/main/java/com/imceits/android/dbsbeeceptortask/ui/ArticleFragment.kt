package com.imceits.android.dbsbeeceptortask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.imceits.android.dbsbeeceptortask.data.AutoClearedValue
import com.imceits.android.dbsbeeceptortask.databinding.FragmentArticleBinding
import com.imceits.android.dbsbeeceptortask.di.Injectable
import javax.inject.Inject

class ArticleFragment : Fragment(), Injectable {

    private lateinit var articleViewModel: ArticleViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var binding by AutoClearedValue<FragmentArticleBinding>(this)
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
        binding.include.btnRefresh.setOnClickListener{
            fetchArticle()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articleViewModel = ViewModelProvider(this, viewModelFactory).get(ArticleViewModel::class.java)
        binding.viewModel = articleViewModel
        fetchArticle()
    }

    fun fetchArticle() {
        articleViewModel.fetchArticles().observe(viewLifecycleOwner, Observer {
            binding.resource = it
            it.data?.let { data ->
                adapter.setData(data)
            }
        })
    }
}
