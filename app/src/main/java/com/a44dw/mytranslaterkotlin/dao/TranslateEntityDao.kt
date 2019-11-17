package com.a44dw.mytranslaterkotlin.dao

import androidx.room.*
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity

@Dao
interface TranslateEntityDao {
    @Query("SELECT * FROM TranslateEntity")
    fun getAll(): List<TranslateEntity>

    @Insert
    fun insert(entity: TranslateEntity): Long

    @Delete
    fun delete(entity: TranslateEntity): Unit

    @Update
    fun update(entity: TranslateEntity): Unit
}