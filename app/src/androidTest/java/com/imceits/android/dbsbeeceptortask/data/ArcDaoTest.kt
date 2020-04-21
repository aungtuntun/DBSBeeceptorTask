package com.imceits.android.dbsbeeceptortask.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class ArcDaoTest{
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var observer: Observer<List<Article>>
    @Mock
    private lateinit var observerSingle: Observer<Article>

    private lateinit var database: ArcDatabase
    private lateinit var arcDao: ArcDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, ArcDatabase::class.java).allowMainThreadQueries().build()
        arcDao = database.arcDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun queryArticleListTest() {
        val article = Article(1, "article 1 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")

        arcDao.insert(listOf(article))
        arcDao.getArticle().observeForever(observer)
        verify(observer).onChanged(listOf(article))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun getArticle() {
        val article = Article(1, "article 1 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")
        arcDao.save(article)
        arcDao.get(1).observeForever(observerSingle)

        verify(observerSingle).onChanged(article)
        verifyNoMoreInteractions(observerSingle)
    }
}