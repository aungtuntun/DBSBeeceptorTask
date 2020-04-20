package com.imceits.android.dbsbeeceptortask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imceits.android.dbsbeeceptortask.ui.ArticleViewModel
import com.imceits.android.dbsbeeceptortask.ui.DetailViewModel
import com.imceits.android.dbsbeeceptortask.ui.UpdateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindArcViewModelFactory(arcViewModelFactory: ArcViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    abstract fun bindArticleViewModel(articleViewModel: ArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateViewModel::class)
    abstract fun bindUpdateViewModel(updateViewModel: UpdateViewModel): ViewModel
}