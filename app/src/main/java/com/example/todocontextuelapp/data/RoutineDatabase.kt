package com.example.todocontextuelapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Routine::class], version = 3)
abstract class RoutineDatabase : RoomDatabase() {
    abstract val routineDao: RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: RoutineDatabase? = null
        private const val DATABASE_NAME = "routine_database.db"

        // Migration de la version 2 à la version 3 :
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Ajoute la colonne "minute" avec une valeur par défaut de 0
                database.execSQL("ALTER TABLE routine_table ADD COLUMN minute INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): RoutineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoutineDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
