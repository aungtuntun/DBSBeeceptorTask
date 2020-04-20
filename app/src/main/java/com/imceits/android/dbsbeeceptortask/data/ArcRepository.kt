package com.imceits.android.dbsbeeceptortask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArcRepository @Inject constructor(private val apiService: APIService, private val arcDao: ArcDao,
                                            private val appExecutors: AppExecutors) {

    fun loadArticles(): LiveData<Resource<List<Article>>> {
        return object : NetworkBoundResource<List<Article>, List<Article>> (appExecutors) {
            override fun saveCallResult(item: List<Article>) {
                arcDao.insert(item)
            }

            override fun shouldFetch(data: List<Article>?): Boolean {
                return data.isNullOrEmpty()
            }

            override fun loadFromDb(): LiveData<List<Article>> {
                return arcDao.getArticle()
            }

            override fun createCall(): Call<List<Article>> {
                return apiService.getArticles()
            }

        }.asLiveData()
    }

    fun saveArticle(item: Article): LiveData<Long> {
        val result = MutableLiveData<Long>()
        appExecutors.diskIO().execute {
            val time = Calendar.getInstance().timeInMillis
            item.last_update = time
            val id = arcDao.save(item)
            result.postValue(id)
        }
        return result
    }

    fun getArticle(id: Int): LiveData<Article> {
        return arcDao.get(id)
    }
}