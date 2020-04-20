package com.imceits.android.dbsbeeceptortask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.imceits.android.dbsbeeceptortask.data.AbsentLiveData
import com.imceits.android.dbsbeeceptortask.data.ArcRepository
import com.imceits.android.dbsbeeceptortask.data.Article
import javax.inject.Inject

class DetailViewModel @Inject constructor(val arcRepository: ArcRepository) : ViewModel() {

    private val params = MutableLiveData<Int>()
    val article: LiveData<Article> = Transformations
        .switchMap(params) {
            if (it == 0) {
                AbsentLiveData.create()
            }else {
                arcRepository.getArticle(it)
            }
        }

    fun updateParam(id: Int) {
        if (params.value == id) {
            return
        }
        params.value = id
    }
}