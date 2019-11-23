package com.a44dw.mytranslaterkotlin.service

import android.content.Context
import com.a44dw.mytranslaterkotlin.interfaces.TranslaterResponseListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class TranslaterService(context: Context) {

    val TRANSLATE_KEY = "trnsl.1.1.20190206T175808Z.634259c03aae2564.997ca9e90c780d7febb1b4b7364432d4ed27f745"
    val TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?"

    lateinit var languageMap: Map<String, String>
    lateinit var listener: TranslaterResponseListener
    lateinit var translateBuilder: StringBuilder
    lateinit var queue: RequestQueue

    init {
        initLanguageMap()
        initRequestQueue(context)
    }

    companion object {
        const val RESULT_OK = 1
        const val RESULT_ERROR = 2
    }

    private fun initLanguageMap() {
        languageMap = mapOf("английский" to "en", "русский" to "ru", "немецкий" to "de",
            "французский" to "fr", "испанский" to "es", "польский" to "pl")
    }

    private fun initRequestQueue(context: Context) {
        queue = Volley.newRequestQueue(context)
    }

    fun translate(textToTranslate: String, from: String?, to: String) {
        val url = concatUrl(textToTranslate, languageMap[from], languageMap[to] ?: error("could not find language $to"))
        val request = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            try {
                val json = JSONObject(it)
                val text = json.getString("text")
                listener.onTranslate(text.substring(2, text.length - 2), RESULT_OK)
            } catch (ex: Throwable) {
                listener.onTranslate("Некорректный ответ от сервиса: $ex", RESULT_ERROR)
            }
        }, Response.ErrorListener {
                listener.onTranslate("Нет связи с сервисом!", RESULT_ERROR)
        })
        queue.add(request)
    }

    private fun concatUrl(textToTranslate: String, from: String?, to: String): String {
        // TODO replace in Kotlin style
        val txt = "&text="
        val lng = "&lang="
        val fromTo = if (from == null) to else "$from-$to"
        initTranslateBuilder()
        translateBuilder.append(txt)
        translateBuilder.append(textToTranslate)
        translateBuilder.append(lng)
        translateBuilder.append(fromTo)

        return translateBuilder.toString()
    }

    private fun initTranslateBuilder() {
        translateBuilder = StringBuilder(TRANSLATE_URL)
        translateBuilder.append("key=")
        translateBuilder.append(TRANSLATE_KEY)
    }

}