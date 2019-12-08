package com.a44dw.mytranslaterkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.a44dw.mytranslaterkotlin.R
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.OnDeleteListener


class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private val maxElements = 9

    lateinit var data: List<TranslateEntity>
    lateinit var listener: OnDeleteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val listItem: CardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_list_item, parent, false) as CardView
        return MainViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int {
        return if (data.size > maxElements) maxElements else data.size
    }

    class MainViewHolder(private val layout: CardView): RecyclerView.ViewHolder(layout) {

        private val originalLang: TextView = layout.findViewById(R.id.fromLanguageTextView)
        private val translatedLang: TextView = layout.findViewById(R.id.toLanguageTextView)
        private val originalText: TextView = layout.findViewById(R.id.originalTextView)
        private val translatedText: TextView = layout.findViewById(R.id.translatedTextView)
        private val bascket: ImageView = layout.findViewById(R.id.basketImageView)

        fun bind(data: TranslateEntity, listener: OnDeleteListener) {
            layout.tag = data
            originalLang.text = data.originalLanguage
            translatedLang.text = data.translatedLanguage
            originalText.text = data.originalText
            translatedText.text = data.translatedText
            bascket.setOnClickListener { listener.onDelete(layout) }
        }
    }
}