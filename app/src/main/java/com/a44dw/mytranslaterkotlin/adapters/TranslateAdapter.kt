package com.a44dw.mytranslaterkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.a44dw.mytranslaterkotlin.R
import com.a44dw.mytranslaterkotlin.entities.TranslateEntity
import com.a44dw.mytranslaterkotlin.interfaces.OnDeleteListener

class TranslateAdapter: RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder>() {

    lateinit var data: List<TranslateEntity>
    lateinit var listener: OnDeleteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslateViewHolder {
        val listItem: ConstraintLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.translate_list_item, parent, false) as ConstraintLayout
        return TranslateViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: TranslateViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class TranslateViewHolder(val layout: ConstraintLayout): RecyclerView.ViewHolder(layout) {

        val originalText: TextView = layout.findViewById(R.id.translateFrom)
        val translateText: TextView = layout.findViewById(R.id.translateTo)
        val bascket: ImageView = layout.findViewById(R.id.basketImageView)

        fun bind(data: TranslateEntity, listener: OnDeleteListener) {
            layout.tag = data
            originalText.text = data.originalText
            translateText.text = data.translatedText
            bascket.setOnClickListener { listener.onDelete(layout) }
        }
    }
}