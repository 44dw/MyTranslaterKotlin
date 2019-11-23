package com.a44dw.mytranslaterkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.a44dw.mytranslaterkotlin.dao.TranslateEntityDao
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity

@Database(entities = [TranslateEntity::class], version = 1, exportSchema = false)
abstract class TranslateDatabase: RoomDatabase() {
    abstract fun translateEntityDao(): TranslateEntityDao

    companion object {
        lateinit var INSTANCE: TranslateDatabase

        fun getDatabase(context: Context): TranslateDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, TranslateDatabase::class.java, "TranslateDatabase").build()
            }

            return INSTANCE
        }
    }
}