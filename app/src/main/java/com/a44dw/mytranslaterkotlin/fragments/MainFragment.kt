package com.a44dw.mytranslaterkotlin.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a44dw.mytranslaterkotlin.ENGLISH_PREF
import com.a44dw.mytranslaterkotlin.R
import com.a44dw.mytranslaterkotlin.RUSSIAN_PREF
import com.a44dw.mytranslaterkotlin.adapters.MainAdapter
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.OnDeleteListener
import com.a44dw.mytranslaterkotlin.model.TranslateViewModel

class MainFragment : Fragment(), View.OnClickListener, OnDeleteListener {
    val mainAdapter: MainAdapter = MainAdapter()

    lateinit var model: TranslateViewModel

    lateinit var translateEntityCollection: LiveData<List<TranslateEntity>>
    lateinit var mainLanguageSpinnerFrom: Spinner
    lateinit var mainLanguageSpinnerTo: Spinner
    lateinit var mainRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val holder = inflater.inflate(R.layout.fragment_main, container, false)

        initModelAndData()
        initUi(holder)

        return holder
    }

    private fun initModelAndData() {
        activity?.let{
            model = ViewModelProviders.of(it)[TranslateViewModel::class.java]
            translateEntityCollection = model.getTranslateEntityCollection()
            translateEntityCollection.observe(this, Observer<List<TranslateEntity>> { translateEntities ->
                updateAndSetAdapter(translateEntities, mainAdapter, mainRecyclerView)
            })
        }
    }

    private fun initUi(holder: View?) {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_spinner_item,
            model.getLanguages())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        holder?.let {
            mainLanguageSpinnerFrom = holder.findViewById(R.id.mainLanguageSpinner1)
            mainLanguageSpinnerFrom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    view?.let {
                        val rawFrom: String = (it as TextView).text.toString()
                        model.updateTranslateFrom(rawFrom)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            mainLanguageSpinnerTo = holder.findViewById(R.id.mainLanguageSpinner2)
            mainLanguageSpinnerTo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    view?.let {
                        val rawTo: String = (it as TextView).text.toString()
                        model.updateTranslateTo(rawTo)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            mainLanguageSpinnerFrom.adapter = adapter
            mainLanguageSpinnerTo.adapter = adapter
            setSpinnerSelections(mainLanguageSpinnerFrom, mainLanguageSpinnerTo)
            val mainReverseButton: ImageButton = holder.findViewById(R.id.mainReverseButton)
            mainReverseButton.setOnClickListener(this)
            mainRecyclerView = holder.findViewById(R.id.mainRecyclerView)
            mainRecyclerView.layoutManager = LinearLayoutManager(activity)
            val autoLanguageCheckBox: CheckBox = holder.findViewById(R.id.autoLanguageCheckBox)
            autoLanguageCheckBox.setOnCheckedChangeListener { _, isChecked ->
                mainLanguageSpinnerFrom.isEnabled = !isChecked
                model.autoFrom = isChecked
            }
        }
    }

    private fun setSpinnerSelections(
        mainLanguageSpinnerFrom: Spinner,
        mainLanguageSpinnerTo: Spinner
    ) {
        val from: String = model.getTranslateFrom().value ?: ENGLISH_PREF
        val to: String = model.getTranslateTo().value ?: RUSSIAN_PREF

        mainLanguageSpinnerFrom.setSelection(model.getLanguages().indexOf(from))
        mainLanguageSpinnerTo.setSelection(model.getLanguages().indexOf(to))
    }

    private fun updateAndSetAdapter(
        translateEntities: List<TranslateEntity>,
        mainAdapter: MainAdapter,
        mainRecyclerView: RecyclerView
    ) {
        mainAdapter.data = translateEntities
        mainAdapter.listener = this
        mainRecyclerView.adapter = mainAdapter
    }

    override fun onDelete(item: View) {
        val entityToDelete: TranslateEntity = item.tag as TranslateEntity
        model.deleteTranslateEntity(entityToDelete)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.mainReverseButton) {
            reverseLanguages()
        }
    }

    private fun reverseLanguages() {
        val fromPosition: Int = mainLanguageSpinnerFrom.selectedItemPosition
        val toPosition: Int = mainLanguageSpinnerTo.selectedItemPosition
        mainLanguageSpinnerFrom.setSelection(toPosition)
        mainLanguageSpinnerTo.setSelection(fromPosition)
    }
}