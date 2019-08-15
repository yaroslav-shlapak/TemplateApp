package com.company.app.di.modules

import com.company.app.core.navigation.ActivityNavigation
import com.company.app.core.navigation.FragmentNavigation
import com.company.app.core.navigation.IActivityNavigation
import com.company.app.core.navigation.IFragmentNavigation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created on 2019/02/25.
 */
@Module
class NavigationModule {
    @Singleton
    @Provides
    internal fun providesActivityNavigation(): ActivityNavigation {
        return IActivityNavigation()
    }

    @Singleton
    @Provides
    internal fun providesFragmentNavigation(): FragmentNavigation {
        return IFragmentNavigation()
    }

}