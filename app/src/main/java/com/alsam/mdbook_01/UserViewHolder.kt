package com.alsam.mdbook_01

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserViewHolder(itemView: View, private val clickListener: (String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    val problemId: TextView = itemView.findViewById(R.id.problemid)
    val title: TextView = itemView.findViewById(R.id.titleTextView)
    val description: TextView = itemView.findViewById(R.id.descriptionTextView)
    val date: TextView = itemView.findViewById(R.id.selectedDateTextView)
    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val userId = problemId.text.toString()
                clickListener(userId)
            }
        }
    }
}
