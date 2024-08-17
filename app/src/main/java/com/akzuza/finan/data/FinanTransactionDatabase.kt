package com.akzuza.finan.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    version = 2,
    entities = [FinanTransaction::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = FinanTransactionDatabase.FakeMigration::class)
    ]
)
abstract class FinanTransactionDatabase: RoomDatabase() {
    abstract fun dao(): FinanTransactionDao

    @RenameColumn(
        tableName = "transactions",
        fromColumnName = "unusedId",
        toColumnName = "id"
    )
    class FakeMigration : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            super.onPostMigrate(db)
        }
    }

}
