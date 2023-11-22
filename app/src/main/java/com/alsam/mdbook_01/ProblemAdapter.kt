package com.alsam.mdbook_01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProblemAdapter (val c: Context, val userList:ArrayList<ProblemAdapter>):RecyclerView.Adapter<ProblemAdapter.UserViewHolder>()

{

    inner class UserViewHolder(val v:View): RecyclerView.ViewHolder(v){

        val titleTextView = v.findViewById<TextView>(R.id.titleTextView)
        val descriptionTextView = v.findViewById<TextView>(R.id.descriptionTextView)
        val selectedDateTextView = v.findViewById<TextView>(R.id.selectedDateTextView)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val v =inflater.inflate(R.layout.list_item,parent,false)

         return UserViewHolder(v)
    }



    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    override fun getItemCount(): Int {
    return userList.size
}
}