package com.cdek.courier.di.module.builder

import com.cdek.courier.delayedProcesses.MainWorker
import com.cdek.courier.delayedProcesses.MyFirebaseMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ServiceBuilder {
    @ContributesAndroidInjector
    internal abstract fun contributeMyFirebaseMessagingService(): MyFirebaseMessagingService

    @ContributesAndroidInjector
    abstract fun contributeMainWorker(): MainWorker
}