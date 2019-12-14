package com.a44dw.mytranslaterkotlin.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.a44dw.mytranslaterkotlin.database.TranslateDatabase
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.TranslaterResponseListener
import com.a44dw.mytranslaterkotlin.repositories.DataRepository
import com.a44dw.mytranslaterkotlin.service.TranslaterService
import kotlinx.coroutines.*

class TranslateViewModel(application : Application) : AndroidViewModel(application),
    TranslaterResponseListener {

    val translateFrom: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val translateTo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val translateResult: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val hotTranslateChecked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val translateEntityCollection: MutableLiveData<MutableList<TranslateEntity>> by lazy {
        MutableLiveData<MutableList<TranslateEntity>>()
    }
    val matchesTranslateEntities: MutableLiveData<MutableList<TranslateEntity>> by lazy {
        MutableLiveData<MutableList<TranslateEntity>>()
    }
    private lateinit var textToTranslate: String

    var autoFrom: Boolean = false
    private val dataRepository: DataRepository = DataRepository(TranslateDatabase.getDatabase(application)!!)
    private val translaterService: TranslaterService = TranslaterService(application)
    private val job: Job = Job()
    // TODO try viewModelScope here
    private val scope = CoroutineScope(job + Dispatchers.Main)

    init {
        translaterService.listener = this
        scope.launch {
            loadTranslateEntityCollection()
        }
    }

    private suspend fun loadTranslateEntityCollection() {
        // переписано с TranslateEntitiesLoader
        val sortedTranslateEntities = dataRepository.getTranslateEntities().sortedBy { entity -> entity.id }
        translateEntityCollection.value = sortedTranslateEntities.toMutableList()
    }

    override fun onTranslate(translateResult: String, status: Int) {
        if (status == TranslaterService.RESULT_OK) {
            this.translateResult.value = translateResult
            if (hotTranslateChecked.value == true) {
                return
            }
            insertTranslateResultInDb()
        }
    }

    fun prepareTranslate(text: String) {
        textToTranslate = text.toLowerCase()
        findMatchesEntities()
        hotTranslateChecked.value?.let {
            if (it) {
                provideTranslate()
            }
        }
    }

    private fun findMatchesEntities() {
        // переписано с MatchesEntitiesLoader
        if (textToTranslate.isEmpty()) {
            return
        }
        // отличие viewModelScope от GlobalScope: когда ViewModel уничтожается, все запущенные корутины
        // автоматически отменяются.
        // https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471
        scope.launch {
            val allEntities: MutableList<TranslateEntity>? = translateEntityCollection.value
            val filtered = allEntities?.filter {
                it.originalText.startsWith(textToTranslate) || it.translatedText.startsWith(textToTranslate)
            }
            filtered?.let {
                matchesTranslateEntities.value = filtered.toMutableList()
            }
        }
    }

    fun provideTranslate() {
        if (textToTranslate.isEmpty()) {
            return
        }
        val translateFrom: String? = if (autoFrom) null else translateFrom.value
        translateTo.value?.let {translateTo ->
            translaterService.translate(textToTranslate, translateFrom, translateTo)
        }
    }

    fun insertTranslateResultInDb(): Boolean {
        val entity = TranslateEntity(
            originalText = textToTranslate,
            translatedText = translateResult.value!!,
            originalLanguage = translateFrom.value!!,
            translatedLanguage = translateTo.value!!
        )
        if (checkTranslateResultExists(entity)) {
            return false
        }

        scope.launch {
            dataRepository.insertTranslateEntity(entity)
            loadTranslateEntityCollection()
        }

        return true
    }

    private fun checkTranslateResultExists(entity: TranslateEntity): Boolean {
        return translateEntityCollection.value?.contains(entity) ?: false
    }

    fun getLanguages(): List<String> {
        return translaterService.languageMap.keys.toList()
    }

    fun deleteTranslateEntity(entityToDelete: TranslateEntity) {
        scope.launch {
            dataRepository.deleteTranslateEntity(entityToDelete)
            loadTranslateEntityCollection()
        }
    }

    fun clearResultAndMatches() {
        translateResult.value = ""
        matchesTranslateEntities.value = emptyList<TranslateEntity>().toMutableList()
    }
}