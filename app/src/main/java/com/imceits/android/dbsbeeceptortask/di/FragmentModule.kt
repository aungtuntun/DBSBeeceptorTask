package com.imceits.android.dbsbeeceptortask.di

import com.imceits.android.dbsbeeceptortask.ui.ArticleFragment
import com.imceits.android.dbsbeeceptortask.ui.UpdateFragment
import com.imceits.android.dbsbeeceptortask.ui.DetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeArticleFragment(): ArticleFragment
    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
    @ContributesAndroidInjector
    abstract fun contributeUpdateFragment(): UpdateFragment
}