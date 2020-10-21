package com.cdek.courier.di.module

import androidx.room.Room
import com.cdek.courier.App
import com.cdek.courier.data.local.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase {
        return Room.databaseBuilder(
            App.instance.applicationContext,
            AppDatabase::class.java,
            "cdek2_db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
//            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun getCatalogReasonDao(appDatabase: AppDatabase): CatalogReasonDao {
        return appDatabase.catalogReasonDao()
    }

    @Provides
    @Singleton
    fun getUpdateDao(appDatabase: AppDatabase): UpdateDao {
        return appDatabase.updateDao()
    }

    @Provides
    @Singleton
    fun getNotificationDao(appDatabase: AppDatabase): NotificationDao {
        return appDatabase.notificationDao()
    }

    @Provides
    @Singleton
    fun getTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun getOperationDao(appDatabase: AppDatabase): OperationDao {
        return appDatabase.operationDao()
    }

    @Provides
    @Singleton
    fun getFileDao(appDatabase: AppDatabase): FileDao {
        return appDatabase.fileDao()
    }
}