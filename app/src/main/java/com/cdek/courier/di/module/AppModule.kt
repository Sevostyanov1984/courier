package com.cdek.courier.di.module

import com.cdek.courier.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class
    ]
)
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(): App {
        return App.instance
    }
}