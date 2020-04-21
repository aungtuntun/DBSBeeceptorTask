package com.imceits.android.dbsbeeceptortask.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.imceits.android.dbsbeeceptortask.Error404NotFoundMatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@RunWith(JUnit4::class)
class NetworkBoundResourceTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()

    companion object {
        data class Foo(val value: Int)

        interface FooAPIService {
            @GET("foo")
            fun getFoo(): Call<Foo>
        }
    }

    @Mock
    private lateinit var observer: Observer<Resource<Foo>>
    private lateinit var appExecutors: AppExecutors
    private lateinit var networkBoundResource: NetworkBoundResource<Foo, Foo>
    private lateinit var dbData: MutableLiveData<Foo>

    private lateinit var saveNetworkCallResult: Function<Foo?, Unit>
    private lateinit var shouldFetchFromNetwork: Function<Foo?, Boolean>
    private lateinit var createNetworkCall: Function<Unit, Call<Foo>>

    private lateinit var fooAPIService: FooAPIService
    private lateinit var mockWebServer: MockWebServer


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        appExecutors = InstantAppExecutors()
        dbData = MutableLiveData()
        networkBoundResource = object : NetworkBoundResource<Foo, Foo> (appExecutors) {
            override fun saveCallResult(item: Foo) {
                saveNetworkCallResult.apply(item)
            }

            override fun shouldFetch(data: Foo?): Boolean {
                return shouldFetchFromNetwork.apply(data)
            }

            override fun loadFromDb(): LiveData<Foo> {
                return dbData
            }

            override fun createCall(): Call<Foo> {
                return createNetworkCall.apply(Unit)
            }
        }
        mockWebServer = MockWebServer()
        mockWebServer.start()

        fooAPIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .build()
            .create(FooAPIService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun successWithNetwork() {
        val saved = AtomicReference<Foo>()
        shouldFetchFromNetwork = Function { foo -> foo == null }
        saveNetworkCallResult = Function { foo -> saved.set(foo); dbData.value = foo}

        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.loading(null)))
        reset(observer)

        val networkResult = Foo(1)
        createNetworkCall = Function { fooAPIService.getFoo() }
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(networkResult)))

        dbData.value = null
        Assert.assertEquals(saved.get(), networkResult)
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.success(networkResult)))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun failureWithNetwork() {
        val saved = AtomicBoolean(false)
        shouldFetchFromNetwork = Function { foo -> foo == null }
        saveNetworkCallResult = Function { foo -> saved.set(true); dbData.value = foo }

        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(Resource.loading(null)))
        reset(observer)

        createNetworkCall = Function { fooAPIService.getFoo() }

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(404))

        dbData.value = null
        Assert.assertFalse(saved.get())
        verify(observer).onChanged(ArgumentMatchers.argThat(Error404NotFoundMatcher()))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithoutNetwork() {
        val saved = AtomicBoolean(false)
        shouldFetchFromNetwork = Function{it == null}
        saveNetworkCallResult = Function{saved.set(true)}

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        val dbFoo = Foo(1)
        dbData.value = dbFoo
        verify(observer).onChanged(Resource.success(dbFoo))
        assertThat(saved.get(), `is`(false))
        val dbFoo2 = Foo(2)
        dbData.value = dbFoo2
        verify(observer).onChanged(Resource.success(dbFoo2))
        verifyNoMoreInteractions(observer)
    }

}