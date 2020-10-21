package com.cdek.courier.di.module.builder

import com.cdek.courier.ui.base.BaseActivity
import com.cdek.courier.ui.photo.sign.SignActivity
import com.cdek.courier.ui.start.StartActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [ViewModelBuilder::class]
)
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [StartFragmentBuildersModule::class])
    internal abstract fun contributeStartActivity(): StartActivity

    @ContributesAndroidInjector(modules = [BaseFragmentBuildersModule::class])
    internal abstract fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector
    internal abstract fun contributeSignActivity(): SignActivity
}