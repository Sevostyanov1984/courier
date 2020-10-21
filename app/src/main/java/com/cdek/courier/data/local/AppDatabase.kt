package com.cdek.courier.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cdek.android.kotlinapp.source.models.entities.*
import com.cdek.courier.data.models.entities.FileEntity
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.data.models.entities.Operation
import com.cdek.courier.data.models.entities.UpdateEntity
import com.cdek.courier.data.models.entities.task.Task


@Database(
    entities = arrayOf(
        CatalogReason::class,
        FileEntity::class,
        UpdateEntity::class,
        Notification::class,
        Task::class,
        Operation::class
    ), version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catalogReasonDao(): CatalogReasonDao
    abstract fun notificationDao(): NotificationDao
    abstract fun taskDao(): TaskDao
    abstract fun updateDao(): UpdateDao
    abstract fun operationDao(): OperationDao
    abstract fun fileDao(): FileDao

    object MIGRATION_1_2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `files` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + "`orderNumber` TEXT, `filePath` TEXT, `posted` INTEGER NOT NULL)"
            )
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `updates` (`id` INTEGER NOT NULL, "
                        + "`entityName` TEXT, `version_id` TEXT, `updateTime` TEXT, PRIMARY KEY(`id`))"
            )
        }
    }
}