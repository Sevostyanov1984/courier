package com.cdek.courier.di.module.builder

import com.cdek.courier.ui.base.gallery.GalleryFragment
import com.cdek.courier.ui.base.map.YandexMapFragment
import com.cdek.courier.ui.base.notification.NotificationFragment
import com.cdek.courier.ui.base.partial.PartialFragment
import com.cdek.courier.ui.base.tasklist.TaskListFragment
import com.cdek.courier.ui.base.task.TaskFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class BaseFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun getTaskListFragment(): TaskListFragment

    @ContributesAndroidInjector
    abstract fun getTaskFragment(): TaskFragment

    @ContributesAndroidInjector
    abstract fun getNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun getGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun getPartialFragment(): PartialFragment

    @ContributesAndroidInjector
    abstract fun getYandexMap(): YandexMapFragment
}