package com.cdek.courier.di.module.builder

import com.cdek.courier.ui.start.auth.AuthFragment
import com.cdek.courier.ui.start.init.InitFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class StartFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun getInitFragment(): InitFragment

    @ContributesAndroidInjector
    abstract fun getAuthFragment(): AuthFragment
}