package com.deadely.itgenglish.utils

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class CustomAdapter<T : Parcelable>(
    @LayoutRes private val itemRes: Int,
    private val onBind: (RecyclerView.ViewHolder, T, T, T) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<T> = mutableListOf()
        private set

    fun setData(list: List<T>) {
        this.list.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(itemRes, parent, false)
        ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        onBind(holder, list[position.rem(list.size)], list.first(), list.last())

    override fun getItemCount(): Int = list.size
}
