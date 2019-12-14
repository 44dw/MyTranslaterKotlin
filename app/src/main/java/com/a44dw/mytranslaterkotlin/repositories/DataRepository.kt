package com.a44dw.mytranslaterkotlin.repositories

import com.a44dw.mytranslaterkotlin.database.TranslateDatabase
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity

class DataRepository(val database: TranslateDatabase) {
    suspend fun insertTranslateEntity(entity: TranslateEntity) {
        database.translateEntityDao().insert(entity)
    }

    suspend fun getTranslateEntities(): List<TranslateEntity> {
        return database.translateEntityDao().getAll()
    }

    suspend fun deleteTranslateEntity(entity: TranslateEntity) {
        database.translateEntityDao().delete(entity)
    }
}