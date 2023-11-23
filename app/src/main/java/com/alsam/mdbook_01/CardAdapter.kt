package com.alsam.mdbook_01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private var dataList: List<CardItem>, private val cardClickListener: (Int) -> Unit) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val selectedDateTextView: TextView = itemView.findViewById(R.id.selectedDateTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            cardClickListener(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = dataList[position]

        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.selectedDateTextView.text = currentItem.selectedDate

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun setDataList(newList: List<CardItem>) {
        dataList = newList
        notifyDataSetChanged()
    }
}
