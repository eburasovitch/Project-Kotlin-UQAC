package com.example.todocontextuelapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Routine::class], version = 2)
abstract class RoutineDatabase : RoomDatabase() {
    abstract val routineDao: RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: RoutineDatabase? = null
        private const val DATABASE_NAME = "routine_database.db"

        fun getInstance(context: Context): RoutineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoutineDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
