package com.alsam.mdbook_01

// UserAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val userList: List<Pair<String, Map<String, Any>>>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (userId, userData) = userList[position]

        // Assuming "title" and "description" are keys in your Firestore document
        val title = userData["title"] as? String ?: "No Title"
        val description = userData["description"] as? String ?: "No Description"
        val date = userData["date"] as? String ?: "Select Date"

        holder.date.text = "$date"
        holder.description.text = "$description"
        holder.problemId.text = "$userId"
        holder.title.text = "$title"
    }
    override fun getItemCount(): Int {
        return userList.size
    }
}
