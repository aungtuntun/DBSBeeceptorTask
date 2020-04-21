package com.imceits.android.dbsbeeceptortask.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.imceits.android.dbsbeeceptortask.data.ArcRepository
import com.imceits.android.dbsbeeceptortask.data.Article
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

//@RunWith(JUnit4::class)
class DetailViewModelTest {

    @Rule
    @JvmField
    var instantExecutor = InstantTaskExecutorRule()
    @Mock
    lateinit var repository: ArcRepository
    @Mock
    lateinit var observer: Observer<Article>
    lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = DetailViewModel(repository)
    }

   // @Test
    fun getArticle() {
        val article1 = Article(1, "article 1 title", 1586404611, "This is article 1 short description. She wholly fat who window extent either formal." +
                " Removing welcomed civility or hastened is.", "https://minotar.net/avatar/user2")
        val article = MutableLiveData<Article>()
        article.value = article1

        `when`(repository.getArticle(1)).thenReturn(article)
        viewModel.article.observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.refEq(article1))
    }

    @After
    fun tearDown() {
    }
}