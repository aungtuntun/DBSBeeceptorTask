package com.imceits.android.dbsbeeceptortask.data

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>
  constructor(private val appExecutors: AppExecutors){

     private val result = MediatorLiveData<Resource<ResultType>>()
    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) {data ->
            result.removeSource(dbSource)
            if (shouldFetch(data))
                fetchFromNetwork(dbSource)
            else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) {newData ->
            setValue(Resource.loading(newData))
        }
        apiResponse.enqueue(object : Callback<RequestType> {
            override fun onFailure(call: Call<RequestType>, t: Throwable) {
                onFetchFailed()
                result.removeSource(dbSource)
                result.addSource(dbSource) {newData ->
                    setValue(Resource.error(t.message!!, newData))
                }
            }

            override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
                appExecutors.diskIO().execute {
                    saveCallResult(processResponse(response.body()!!))
                    appExecutors.mainThread().execute {
                        result.addSource(loadFromDb()) {
                            setValue(Resource.success(it))
                        }
                    }
                }
            }

        })
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue)
            result.value = newValue
    }

    @WorkerThread
    protected open fun processResponse(response: RequestType) = response
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean
    @MainThread
    protected  abstract fun loadFromDb(): LiveData<ResultType>
    @MainThread
    protected abstract fun createCall(): Call<RequestType>

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected open fun onFetchFailed(){}
}