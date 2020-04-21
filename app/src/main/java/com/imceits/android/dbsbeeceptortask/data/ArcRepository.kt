package com.imceits.android.dbsbeeceptortask.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ArcRepository @Inject constructor(private val apiService: APIService, private val arcDao: ArcDao,
                                             private val appExecutors: AppExecutors) {

   open fun loadArticles(): LiveData<Resource<List<Article>>> {
        return object : NetworkBoundResource<List<Article>, List<Article>> (appExecutors) {
            override fun saveCallResult(item: List<Article>) {
                arcDao.insert(item)
            }

            override fun shouldFetch(data: List<Article>?): Boolean {
                val shouldFetch = data == null || data.isEmpty()
                Log.d("TAG", "Should fetch $shouldFetch")
                return shouldFetch
            }

            override fun loadFromDb(): LiveData<List<Article>> {
                return arcDao.getArticle()
            }

            override fun createCall(): Call<List<Article>> {
                Log.d("TAG", "Create call network")
                return apiService.getArticles()
            }

        }.asLiveData()
    }

    open fun saveArticle(item: Article): LiveData<Long> {
        val result = MutableLiveData<Long>()
        appExecutors.diskIO().execute {
            val time = Calendar.getInstance().timeInMillis
            item.last_update = time
            val id = arcDao.save(item)
            result.postValue(id)
        }
        return result
    }

    open fun getArticle(id: Int): LiveData<Article> {
        return arcDao.get(id)
    }
}