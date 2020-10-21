package com.cdek.courier.di.module.worker

import androidx.work.Worker
import dagger.internal.Preconditions

object AndroidWorkerInjection {
    fun inject(worker: Worker) {
        Preconditions.checkNotNull(worker, "worker")
        val application: Any = worker.applicationContext
        if (application !is HasWorkerInjector) {
            throw RuntimeException(
                String.format(
                    "%s does not implement %s",
                    application.javaClass.canonicalName,
                    HasWorkerInjector::class.java.canonicalName
                )
            )
        }
        val workerInjector =
            application.workerInjector()
        Preconditions.checkNotNull(
            workerInjector,
            "%s.workerInjector() returned null",
            application.javaClass
        )
        workerInjector.inject(worker)
    }
}