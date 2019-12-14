package com.a44dw.mytranslaterkotlin.dao

import androidx.room.*
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity

@Dao
interface TranslateEntityDao {
    @Query("SELECT * FROM TranslateEntity")
    suspend fun getAll(): List<TranslateEntity>

    @Insert
    suspend fun insert(entity: TranslateEntity): Long

    @Delete
    suspend fun delete(entity: TranslateEntity): Unit

    @Update
    suspend fun update(entity: TranslateEntity): Unit
}