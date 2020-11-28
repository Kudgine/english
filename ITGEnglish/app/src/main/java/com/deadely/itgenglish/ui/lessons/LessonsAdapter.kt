package com.deadely.itgenglish.ui.lessons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deadely.itgenglish.R
import com.deadely.itgenglish.model.Lessons
import com.deadely.itgenglish.ui.lessons.LessonsAdapter.ViewHolder
import kotlinx.android.synthetic.main.row_lessons.view.*

class LessonsAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val list: MutableList<Lessons> = mutableListOf()
    private var listener: OnItemClickListener? = null

    fun setOnClickListener(clickListener: OnItemClickListener) {
        listener = clickListener
    }

    fun setData(newList: List<Lessons>) {
        list.apply {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(lessons: Lessons) {
            with(itemView) {
                tvTitle.text = lessons.title
                itemView.setOnClickListener {
                    listener?.onClick(lessons)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_lessons, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size


    interface OnItemClickListener {
        fun onClick(lessons: Lessons)
    }
}
