package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class PatientProblemList : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_problem_list)

        drawerLayout = findViewById(R.id.drawerLayout2)
        navigationView = findViewById(R.id.navigationView)
        recyclerView = findViewById(R.id.cardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CardAdapter(ArrayList()) { position ->

         var id =    problemList.get(position).id;



            val intent = Intent(this@PatientProblemList, CareGiverRecordActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)



            // Toast.makeText(this, "This is a short toast message", Toast.LENGTH_SHORT).show()


            // Handle card item click here if needed
        }
        recyclerView.adapter = adapter

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    // Handle profile item click
                    true
                }
                R.id.signout -> {
                    val intent = Intent(this@PatientProblemList, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_small_drawer_icon)
        }


        val scannedUserID = intent.getStringExtra("USER_ID")



        if (scannedUserID != null) {
            fetchProblemsForUser(scannedUserID)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    val problemList = mutableListOf<CardItem>();
    private fun fetchProblemsForUser(userID: String) {
        val db = FirebaseFirestore.getInstance()
        val problemsCollection = db.collection("problems")
        val query = problemsCollection.whereEqualTo("userid", userID)

        query.get()
            .addOnSuccessListener { documents ->


                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val date = document.getString("date") ?: ""

                    val problem = CardItem(title, description, date,document.id)
                    problemList.add(problem)
                }

                if (problemList.isNotEmpty()) {
                    adapter.setDataList(problemList) // Update RecyclerView data here
                } else {
                    // Display a message if no problems are found for the user
                    // You can use Toast or any other way to display this message
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures if any
            }
    }

}
