package com.a44dw.mytranslaterkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.a44dw.mytranslaterkotlin.MainActivity
import com.a44dw.mytranslaterkotlin.R
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.model.TranslateViewModel

class MainFragment : Fragment() {
    lateinit var model: TranslateViewModel
    lateinit var translateEntityCollection: LiveData<List<TranslateEntity>>

    lateinit var mainLanguageSpinnerFrom: Spinner
    lateinit var mainLanguageSpinnerTo: Spinner
    lateinit var mainRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val holder = inflater.inflate(R.layout.fragment_main, container, false)

        initModelAndData()
        initUi(holder)
    }

    private fun initUi(holder: View?) {
        context?.let {context ->
            val adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_item, model.getLanguages())
        }
    }

    private fun initModelAndData() {
        activity?.let{
            model = ViewModelProviders.of(it)[TranslateViewModel::class.java]
            translateEntityCollection = model.getTranslateEntityCollection()
            translateEntityCollection.observe(this, Observer<List<TranslateEntity>> { translateEntities ->
                updateAndSetAdapter(translateEntities)
            })
        }
    }

    private fun updateAndSetAdapter(translateEntities: List<TranslateEntity>?) {

    }
}