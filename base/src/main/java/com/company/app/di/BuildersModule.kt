package com.company.app.di

import com.company.app.di.scopes.MainActivityScope
import com.company.app.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @MainActivityScope
    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity
}
