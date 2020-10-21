package com.cdek.courier.di.module.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdek.courier.ui.DaggerViewModelFactory
import com.cdek.courier.ui.base.BaseViewModel
import com.cdek.courier.ui.base.gallery.GalleryViewModel
import com.cdek.courier.ui.base.notification.NotificationViewModel
import com.cdek.courier.ui.base.partial.PartialViewModel
import com.cdek.courier.ui.base.task.TaskViewModel
import com.cdek.courier.ui.base.tasklist.TaskListViewModel
import com.cdek.courier.ui.photo.sign.SignViewModel
import com.cdek.courier.ui.start.auth.AuthViewModel
import com.cdek.courier.ui.start.init.InitViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/*
* Если что не понятно втыкаем сюда:
* https://medium.com/@marco_cattaneo/android-viewmodel-and-factoryprovider-good-way-to-manage-it-with-dagger-2-d9e20a07084c
* и сюда:
* https://dagger.dev/multibindings#map-multibindings
* */


@Module
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel::class)
    abstract fun bindBaseViewModel(baseViewModel: BaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InitViewModel::class)
    abstract fun bindInitViewModel(initViewModel: InitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    abstract fun bindTaskListViewModel(taskListViewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    abstract fun bindTaskViewModel(taskViewModel: TaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(notificationViewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignViewModel::class)
    abstract fun bindSignViewModel(signViewModel: SignViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun bindGalleryViewModel(galleryViewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PartialViewModel::class)
    abstract fun bindPartialViewModel(partialViewModel: PartialViewModel): ViewModel
}