package com.imceits.android.dbsbeeceptortask.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset

@RunWith(JUnit4::class)
class APIServiceTest {

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var apiService: APIService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient())
            .build()
            .create(APIService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
    @Test
    fun getArticle() {
        val articles = this.javaClass.classLoader!!.getResource("api-response/articles.json").readText(
            Charset.forName("UTF-8"))
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(articles))
        val article = Article(1, "article 1 title", 1586404611,
            "This is article 1 short description. She wholly fat who window extent either formal. Removing welcomed civility or hastened is.",
            "https://minotar.net/avatar/user2")
        val response = apiService.getArticles().execute()
        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(response.body())
        Assert.assertEquals(response.body()!!.first(), article)
    }

}