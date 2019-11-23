package com.a44dw.mytranslaterkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a44dw.mytranslaterkotlin.R
import com.a44dw.mytranslaterkotlin.adapters.TranslateAdapter
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.OnDeleteListener
import com.a44dw.mytranslaterkotlin.model.TranslateViewModel
import com.google.android.material.snackbar.Snackbar

class TranslateFragment : Fragment(), View.OnClickListener, OnDeleteListener {

    lateinit var model: TranslateViewModel
    lateinit var translateFrom: LiveData<String>
    lateinit var translateTo: LiveData<String>
    lateinit var translateResult: LiveData<String>
    lateinit var matchesEntities: LiveData<MutableList<TranslateEntity>>

    lateinit var translateResultField: TextView
    lateinit var translateHotSwitch: Switch
    lateinit var translateToButton: Button
    lateinit var translateResultLayout: TextView
    lateinit var translateFromHint: TextView
    lateinit var translateDictionaryHint: TextView
    lateinit var translateSave: ImageView
    lateinit var translateRecyclerView: RecyclerView
    lateinit var translateAdapter: TranslateAdapter

    var isSnackShown: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val holder: View = inflater.inflate(R.layout.fragment_translate, container, false)

        initModelAndData()
        initUi(holder)

        return holder
    }

    private fun initModelAndData() {
        activity?.let {
            model = ViewModelProviders.of(it)[TranslateViewModel::class.java]
            translateFrom = model.translateFrom
            translateFrom.observe(this, Observer<String> { translateFrom ->
                if (!model.autoFrom) {
                    translateFromHint.text = translateFrom
                }
            })
            translateTo = model.translateTo
            translateTo.observe(this, Observer<String> {translateTo ->
                translateToButton.text = translateTo
            })
            translateResult = model.translateResult
            translateResult.observe(this, Observer<String> { translateResult ->
                translateResultLayout.visibility = if (translateResult.isNotEmpty()) View.VISIBLE else View.GONE
            })
            matchesEntities = model.matchesTranslateEntities
            matchesEntities.observe(this, Observer<List<TranslateEntity>> { matchesEntities ->
                translateDictionaryHint.visibility = if (matchesEntities.isNotEmpty()) View.VISIBLE else View.GONE
                translateAdapter.data = matchesEntities
                val manager: LinearLayoutManager = LinearLayoutManager(activity)
                translateRecyclerView.layoutManager = manager
                translateRecyclerView.adapter = translateAdapter
                val dividerItemDecoration: DividerItemDecoration =
                    DividerItemDecoration(translateRecyclerView.context, manager.orientation)
                translateRecyclerView.addItemDecoration(dividerItemDecoration)
            })
        }
    }

    private fun initUi(holder: View) {
        translateResultField = holder.findViewById(R.id.translateTextField)
        translateToButton = holder.findViewById(R.id.translateToButton)
        translateToButton.setOnClickListener(this)
        translateFromHint = holder.findViewById(R.id.translateFromHint)
        translateResultLayout = holder.findViewById(R.id.translateResultLayout)
        translateDictionaryHint = holder.findViewById(R.id.translateDictionaryHint)
        translateHotSwitch = holder.findViewById(R.id.translateHot)
        translateHotSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.hotTranslateChecked.value = isChecked
            translateSave.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (isChecked) showCheckedChangedInfo()
            else isSnackShown = false
        }
        translateSave = holder.findViewById(R.id.translateSave)
        translateSave.setOnClickListener(this)
        translateRecyclerView = holder.findViewById(R.id.translateSearchRecyclerView)
        translateAdapter = TranslateAdapter()
        translateAdapter.listener = this
        model.hotTranslateChecked.value = translateHotSwitch.isChecked

        clearTranslateResultField()
    }

    private fun showCheckedChangedInfo() {
        if (isSnackShown) return
        Snackbar.make(translateHotSwitch,
            resources.getText(R.string.hot_translate_disclaimer),
            Snackbar.LENGTH_LONG
        )
            .show()
    }

    private fun showSaveInfo(saveResult: Boolean) {
        val textId: Int = if (saveResult) R.string.save_success else R.string.save_unsuccess
        val message = resources.getText(textId)
        Snackbar.make(translateSave,
            message,
            Snackbar.LENGTH_LONG
        )
            .show()
    }

    fun clearTranslateResultField() {
        model.clearResultAndMatches()
        translateResultLayout.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.translateToButton -> model.provideTranslate()
            R.id.translateSave -> showSaveInfo(model.insertTranslateResultInDb())
        }
    }

    override fun onDelete(item: View) {
        val entityToDelete: TranslateEntity = item.tag as TranslateEntity
        model.deleteTranslateEntity(entityToDelete)
        matchesEntities.value?.remove(entityToDelete)
        translateAdapter.notifyDataSetChanged()
    }
}