package com.imceits.android.dbsbeeceptortask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.imceits.android.dbsbeeceptortask.data.AbsentLiveData
import com.imceits.android.dbsbeeceptortask.data.ArcRepository
import com.imceits.android.dbsbeeceptortask.data.Article
import javax.inject.Inject

class UpdateViewModel @Inject constructor(private val arcRepository: ArcRepository) : ViewModel() {

    private val param = MutableLiveData<Int>()

    val article : LiveData<Article> = Transformations
        .switchMap(param) {
            if (it == 0) {
                AbsentLiveData.create()
            }else{
                arcRepository.getArticle(it)
            }
        }

    fun update(data: Article): LiveData<Long> {
        return arcRepository.saveArticle(data)
    }

    fun updateParam(id: Int) {
        if (param.value == id) {
            return
        }
        param.value = id
    }
}