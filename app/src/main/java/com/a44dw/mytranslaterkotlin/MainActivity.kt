package com.a44dw.mytranslaterkotlin

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.a44dw.mytranslaterkotlin.fragments.MainFragment
import com.a44dw.mytranslaterkotlin.fragments.TranslateFragment
import com.a44dw.mytranslaterkotlin.model.TranslateViewModel

const val FROM_LANGUAGE_PREFS = "fromLanguagePrefs"
const val TO_LANGUAGE_PREFS = "toLanguagePrefs"
const val MAIN_FRAGMENT_TAG = "main_fragment"
const val TRANSLATE_FRAGMENT_TAG = "translate_fragment"
const val ENGLISH_PREF = "английский"
const val RUSSIAN_PREF = "русский"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var model: TranslateViewModel
    private lateinit var mainFragment: MainFragment
    private lateinit var translateFragment: TranslateFragment
    private lateinit var mainTranslateField: EditText
    private lateinit var mainClear: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initModelAndData()
        initUi()
        initLanguagePrefs()

        showMainFragment()
    }

    private fun initModelAndData() {
        model = ViewModelProviders.of(this)[TranslateViewModel::class.java]
    }

    private fun initUi() {
        mainFragment = MainFragment()
        translateFragment = TranslateFragment()
        mainTranslateField = findViewById(R.id.mainTranslateField)
        mainTranslateField.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) {
                    showTranslateFragment()
                } else {
                    showMainFragment()
                }
            }
        }

        mainTranslateField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                prepareTranslate(s.toString())
            }
        })
        mainClear = findViewById(R.id.mainClear)
        mainClear.setOnClickListener(this)
    }

    private fun initLanguagePrefs() {
        val preferences = getPreferences(Activity.MODE_PRIVATE)
        if (preferences.contains(FROM_LANGUAGE_PREFS) && preferences.contains(TO_LANGUAGE_PREFS)) {
            with(model) {
                translateFrom.value = preferences.getString(FROM_LANGUAGE_PREFS, ENGLISH_PREF)
                translateTo.value = preferences.getString(TO_LANGUAGE_PREFS, RUSSIAN_PREF)
            }
        }
    }

    private fun prepareTranslate(text: String) {
        model.prepareTranslate(text)
    }

    private fun showMainFragment() {
        // доступ к свойству, используется вместо getSupportFragmentManager(), потому что
        // этот метод следует конвенциям геттеров
        // https://kotlinlang.ru/docs/reference/java-interop.html
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentHolder, mainFragment, MAIN_FRAGMENT_TAG)
            .commit()
        mainClear.visibility = View.GONE
    }

    private fun showTranslateFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentHolder, translateFragment, TRANSLATE_FRAGMENT_TAG)
            .commit()
        mainClear.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        v?.id.let {
            if (it == R.id.mainClear) {
                if (mainTranslateField.text.isNotEmpty()) {
                    mainTranslateField.text.clear()
                    translateFragment.clearTranslateResultField()
                } else {
                    mainTranslateField.clearFocus()
                    showMainFragment()
                }
            }
        }
        // другой вариант
//        when (v?.id) {
//            R.id.mainClear -> {
//                if (mainTranslateField.text.isNotEmpty()) {
//                    // нужно ли?
//                    // mainTranslateField.text.clear()
//                    translateFragment.clearTranslateResultField()
//                } else {
//                    mainTranslateField.clearFocus()
//                    showMainFragment()
//                }
//            }
//        }
    }

    override fun onBackPressed() {
        // https://stackoverflow.com/questions/46723729/swift-if-let-statement-in-kotlin
        // https://kotlinlang.ru/docs/reference/null-safety.html
        supportFragmentManager.findFragmentByTag(TRANSLATE_FRAGMENT_TAG)?.let {
            mainTranslateField.clearFocus()
            showMainFragment()
        } ?: super.onBackPressed()
    }
}
