package com.a44dw.mytranslaterkotlin.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TranslateEntity(@PrimaryKey(autoGenerate = true) var id: Long,
                      var originalText: String,
                      var translatedText: String,
                      var originalLanguage: String,
                      var translatedLanguage: String)