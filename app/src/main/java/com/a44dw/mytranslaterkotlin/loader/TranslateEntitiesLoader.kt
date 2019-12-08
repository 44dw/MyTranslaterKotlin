package com.a44dw.mytranslaterkotlin.loader

import android.os.AsyncTask
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.model.TranslateViewModel
import java.lang.ref.WeakReference

class TranslateEntitiesLoader(private val vrModel: WeakReference<TranslateViewModel>): AsyncTask<Void, Void, List<TranslateEntity>>() {
    override fun doInBackground(vararg params: Void?): List<TranslateEntity> {
        return vrModel.get()?.dataRepository?.getTranslateEntities() ?: emptyList()
    }

    override fun onPostExecute(translateEntities: List<TranslateEntity>) {
        val sortedEntities: List<TranslateEntity> = translateEntities.sortedBy { entity -> entity.id }
        vrModel.get()?.let {
            it.translateEntityCollection.value = sortedEntities.toMutableList()
        }
    }
}