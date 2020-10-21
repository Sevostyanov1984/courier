package com.cdek.courier

import androidx.work.*
import com.cdek.courier.delayedProcesses.MainWorker
import com.cdek.courier.di.component.DaggerAppComponent
import com.cdek.courier.di.module.worker.HasWorkerInjector
import com.facebook.stetho.Stetho
//import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class App : DaggerApplication(), HasWorkerInjector {

    @Inject
    lateinit var workerDispatchingAndroidInjector: DispatchingAndroidInjector<Worker>

    //private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()

        //firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        instance = this

        startService()
        stethoInit()
    }

    private fun startService() {
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            MainWorker::class.java, 2, TimeUnit.MINUTES, 1, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(instance).enqueueUniquePeriodicWork(
            "cdekCourierMainWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun workerInjector(): AndroidInjector<Worker> {
        return workerDispatchingAndroidInjector
    }

    private fun stethoInit() {
        //После установки приложения на устройство или на эмуляторе запустите браузер Google Chrome и введите в адресной строке chrome://inspect.
        // Create an InitializerBuilder
        val initializerBuilder = Stetho.newInitializerBuilder(this)

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
            Stetho.defaultInspectorModulesProvider(this)
        )

        // Enable command line interface
        initializerBuilder.enableDumpapp(
            Stetho.defaultDumperPluginsProvider(this)
        )

        // Use the InitializerBuilder to generate an Initializer
        val initializer = initializerBuilder.build()

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer)
    }
}