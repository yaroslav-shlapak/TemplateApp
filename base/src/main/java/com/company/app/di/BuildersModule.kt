package com.company.app.di

import com.company.app.presentation.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributesMainFragmentScope(): MainFragment
}
