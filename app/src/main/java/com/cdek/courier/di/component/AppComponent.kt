package com.cdek.courier.di.component

import com.cdek.courier.App
import com.cdek.courier.di.module.AppModule
import com.cdek.courier.di.module.builder.ActivityBuilder
import com.cdek.courier.di.module.builder.ServiceBuilder
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ServiceBuilder::class,
        ActivityBuilder::class]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Builder : AndroidInjector.Factory<App>
}