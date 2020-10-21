package com.cdek.courier.delayedProcesses

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.cdek.courier.data.Repository
import com.cdek.courier.di.module.worker.AndroidWorkerInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @Inject
    lateinit var repository: Repository

    override fun doWork(): Result {

        AndroidWorkerInjection.inject(this)

        getAll()

        return Result.success()
    }


    fun getAll() {
        if (repository.getPrefUserId().isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = repository.getAll()
                val qwe = result
            }
        }
    }
}