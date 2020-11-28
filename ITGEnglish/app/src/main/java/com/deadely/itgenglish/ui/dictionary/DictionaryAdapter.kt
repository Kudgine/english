package com.deadely.itgenglish.ui.dictionary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deadely.itgenglish.R
import com.deadely.itgenglish.model.Word
import com.deadely.itgenglish.ui.dictionary.DictionaryAdapter.ViewHolder
import kotlinx.android.synthetic.main.row_word.view.*

class DictionaryAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val list: MutableList<Word> = mutableListOf()
    private var listener: OnItemClickListener? = null

    fun setOnClickListener(clickListener: OnItemClickListener) {
        listener = clickListener
    }

    fun setData(newList: List<Word>) {
        list.apply {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(word: Word) {
            with(itemView) {
                tvWord.text = word.word
                tvDescription.text = word.translate
                tvTr.text = word.tr
                if (word.favorite) {
                    ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_red)
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_grey)
                }
                ivFavorite.setOnClickListener {
                    word.favorite = !word.favorite
                    if (word.favorite) {
                        ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_red)
                        listener?.doFavorite(word)
                    } else {
                        ivFavorite.setImageResource(R.drawable.ic_baseline_favorite_grey)
                        listener?.removeFromFavorite(word)
                    }
                }
                ivVolume.setOnClickListener {
                    listener?.onVolumeClick(word)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_word, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size


    interface OnItemClickListener {
        fun doFavorite(word: Word)
        fun removeFromFavorite(word: Word)
        fun onVolumeClick(word: Word)
    }
}
