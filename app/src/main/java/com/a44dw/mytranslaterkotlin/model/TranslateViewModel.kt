package com.a44dw.mytranslaterkotlin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.TranslaterResponseListener

class TranslateViewModel(application : Application) : AndroidViewModel(application),
    TranslaterResponseListener {

    val translateResult: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val matchesTranslateEntities: MutableLiveData<MutableList<TranslateEntity>> by lazy {
        MutableLiveData<MutableList<TranslateEntity>>()
    }
    val hotTranslateChecked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    var autoFrom: Boolean = false

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

    fun getTranslateFrom(): LiveData<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getTranslateTo(): LiveData<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun deleteTranslateEntity(entityToDelete: TranslateEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun clearResultAndMatches() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun provideTranslate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertTranslateResultInDb(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}