package com.a44dw.mytranslaterkotlin.repositories

import com.a44dw.mytranslaterkotlin.database.TranslateDatabase
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity

class DataReporitory(val database: TranslateDatabase) {
    fun insertTranslateEntity(entity: TranslateEntity) {
        database.translateEntityDao().insert(entity)
    }

    fun getTranslateEntities(): List<TranslateEntity> {
        return database.translateEntityDao().getAll()
    }

    fun deleteTranslateEntity(entity: TranslateEntity) {
        database.translateEntityDao().delete(entity)
    }
}