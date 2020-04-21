package com.imceits.android.dbsbeeceptortask.data

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call
import java.io.IOException

abstract class NetworkBoundResource<ResultType, RequestType>
 @MainThread constructor(private val appExecutors: AppExecutors){

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

        result.addSource(dbSource) {newData ->
            setValue(Resource.loading(newData))
        }
        appExecutors.networkIO().execute {
            val apiResponse = createCall().execute()
       try {
           when(apiResponse.isSuccessful) {
               true -> appExecutors.diskIO().execute {
                   saveCallResult(apiResponse.body()!!)
                   appExecutors.mainThread().execute {
                       result.removeSource(loadFromDb())
                       result.addSource(loadFromDb()) {
                           setValue(Resource.success(it))
                       }
                   }
               }
                   false -> appExecutors.mainThread().execute {
                       onFetchFailed()
                       result.removeSource(dbSource)
                       result.addSource(dbSource) {newData ->
                           setValue(Resource.error(apiResponse.message(), newData))
                       }
                   }
           }
       }catch (exc: IOException) {
           Log.e("NETWORK", exc.message!!)
           onFetchFailed()
           result.removeSource(dbSource)
           result.addSource(dbSource) {newData ->
               setValue(Resource.error(exc.message!!, newData))
           }
       }
        }
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