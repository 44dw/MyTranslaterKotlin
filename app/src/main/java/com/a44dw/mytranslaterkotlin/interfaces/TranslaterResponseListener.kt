package com.a44dw.mytranslaterkotlin.interfaces

interface TranslaterResponseListener {
    fun onTranslate(translateResult: String, status: Int)
}