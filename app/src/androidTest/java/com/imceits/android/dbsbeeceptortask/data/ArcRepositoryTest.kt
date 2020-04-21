package com.imceits.android.dbsbeeceptortask.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.*

//@RunWith(AndroidJUnit4::class)
class ArcRepositoryTest {
    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()
    @Mock
    private lateinit var observer: Observer<Resource<List<Article>>>

    private lateinit var mockWebServer: MockWebServer
    private lateinit var database: ArcDatabase
    private lateinit var arcDao: ArcDao
    private lateinit var repository: ArcRepository
    private lateinit var appExecutors: AppExecutors
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: APIService

    companion object {
        val TIMEOUT = 1000L
    }
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, ArcDatabase::class.java).allowMainThreadQueries().build()
        arcDao = database.arcDao()
        appExecutors = AppExecutors()

        mockWebServer = MockWebServer()
        mockWebServer.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(mockWebServer.url("/").toString())
            .client(OkHttpClient())
            .build()
        apiService = retrofit.create(APIService::class.java)
        repository = ArcRepository(apiService, arcDao, appExecutors)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
   // @Test
    fun getArticleWithValidTest() {
        val content = this.javaClass.classLoader!!.getResource("api-repository/articles.json").readText(
            Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(content))

            val article = Article(1, "article 1 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                    " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")

            repository.loadArticles().observeForever(observer)
            val resource = Resource.loading(data = null)
            verify(observer).onChanged(ArgumentMatchers.eq(resource))
            verify(observer, timeout(TIMEOUT)).onChanged(ArgumentMatchers.refEq(Resource.success(Collections.singletonList(article))))
    }
}