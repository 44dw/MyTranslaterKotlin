package com.a44dw.mytranslaterkotlin.loader

import android.os.AsyncTask
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.model.TranslateViewModel
import java.lang.ref.WeakReference

class MatchesEntitiesLoader(private val wrModel: WeakReference<TranslateViewModel>): AsyncTask<Any, Void, List<TranslateEntity>>() {
    override fun doInBackground(vararg params: Any?): List<TranslateEntity> {
        params.let {
            // получает лист TranslateEntity и текст и фильтрует по совпадению с текстом
            val text: String = params[0] as String
            val allEntities: List<TranslateEntity> = params[1] as List<TranslateEntity>

            return allEntities.filter {
                it.originalText.startsWith(text) || it.translatedText.startsWith(text)
            }
        }
    }

    override fun onPostExecute(newMatchesEntities: List<TranslateEntity>) {
        // сеттит получившийся список в модель
        wrModel.get()?.let {
            val origMatchesEntities: List<TranslateEntity> = it.matchesTranslateEntities.value ?: emptyList()

            if ((origMatchesEntities.containsAll(newMatchesEntities))
                && newMatchesEntities.containsAll(origMatchesEntities)) {
                it.matchesTranslateEntities.value = newMatchesEntities.toMutableList()
            }
        }
    }
}