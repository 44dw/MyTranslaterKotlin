package com.a44dw.mytranslaterkotlin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.TranslaterResponseListener

class TranslateViewModel(application : Application) : AndroidViewModel(application),
    TranslaterResponseListener {

    init {

    }

    override fun onTranslate(translateResult: String, status: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun prepareTranslate(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun updateTranslateFrom(string: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun updateTranslateTo(string: String?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getTranslateEntityCollection(): LiveData<List<TranslateEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getLanguages(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}