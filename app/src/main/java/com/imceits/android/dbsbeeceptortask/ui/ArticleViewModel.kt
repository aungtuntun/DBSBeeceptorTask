package com.imceits.android.dbsbeeceptortask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imceits.android.dbsbeeceptortask.data.ArcRepository
import com.imceits.android.dbsbeeceptortask.data.Article
import com.imceits.android.dbsbeeceptortask.data.Resource
import javax.inject.Inject

class ArticleViewModel @Inject constructor(private val arcRepository: ArcRepository) : ViewModel() {
    val param = MutableLiveData<Int>()

    fun fetchArticles(): LiveData<Resource<List<Article>>> {
        return arcRepository.loadArticles()
    }

}