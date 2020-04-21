package com.imceits.android.dbsbeeceptortask.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.imceits.android.dbsbeeceptortask.data.ArcRepository
import com.imceits.android.dbsbeeceptortask.data.Article
import com.imceits.android.dbsbeeceptortask.data.Resource
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ArticleViewModelTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()
    @Mock
    lateinit var repository: ArcRepository
    @Mock
    lateinit var observer: Observer<Resource<List<Article>>>
    lateinit var viewModel: ArticleViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ArticleViewModel(repository)
    }
    @Test
    fun getArticleList() {
        val article1 = Article(1, "article 1 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")
        val article2 = Article(2, "article 2 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")
        val article3 = Article(3, "article 3 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")
        val articles = MutableLiveData<Resource<List<Article>>>()
        val resource = Resource.success(listOf(article1, article2, article3))
        articles.value = resource

        `when`(repository.loadArticles()).thenReturn(articles)
        viewModel.fetchArticles().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(resource))
    }

    @After
    fun tearDown() {
    }
}