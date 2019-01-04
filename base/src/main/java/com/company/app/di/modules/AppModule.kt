package com.company.app.di.modules

import android.app.Application
import android.content.Context
import com.company.app.di.DiConstants
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named


@Module
class AppModule(private val application: Application) {

    @Provides
    @Named(DiConstants.databaseScheduler)
    internal fun provideDatabaseScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(DiConstants.networkScheduler)
    internal fun provideNetworkScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(DiConstants.mainThreadScheduler)
    internal fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Named(DiConstants.timeoutScheduler)
    internal fun provideTimeoutScheduler(): Scheduler {
        return Schedulers.computation()
    }

    @Provides
    internal fun providesApplicationContext(): Context {
        return application.applicationContext
    }

}
