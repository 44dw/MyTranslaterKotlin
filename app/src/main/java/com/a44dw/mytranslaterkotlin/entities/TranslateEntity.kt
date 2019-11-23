package com.a44dw.mytranslaterkotlin.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TranslateEntity(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                      var originalText: String,
                      var translatedText: String,
                      var originalLanguage: String,
                      var translatedLanguage: String)