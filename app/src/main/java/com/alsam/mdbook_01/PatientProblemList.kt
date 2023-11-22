package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class PatientProblemList : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_problem_list)

        drawerLayout = findViewById(R.id.drawerLayout2)
        navigationView = findViewById(R.id.navigationView)

        val cardView: CardView = findViewById(R.id.cardView)
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val selectedDateTextView: TextView = findViewById(R.id.selectedDateTextView)

        // Set values to the TextViews
        titleTextView.text = "Patient"
        descriptionTextView.text = "I had a headache in a library"
        selectedDateTextView.text = "02/09/2023"



        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {


                }
                R.id.signout -> {
                    val intent = Intent(this@PatientProblemList, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_small_drawer_icon)
        }




        // Set click listener to the CardView
        cardView.setOnClickListener {
            // Start the PatientProblemRecord activity on card click
            val intent = Intent(this@PatientProblemList, PatientProblemRecord::class.java)
            startActivity(intent)
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
}

