package com.alsam.mdbook_01


import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var cardList: ArrayList<CardItem>
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        cardRecyclerView = findViewById(R.id.cardRecyclerView)
        cardList = ArrayList()
        cardAdapter = CardAdapter(cardList)
        cardRecyclerView.adapter = cardAdapter

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)


        val filter = IntentFilter("NEW_PROBLEM_ADDED")



        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    // Handle profile item click
                }
                R.id.shareQR -> {
                    val intent = Intent(this@MainActivity, GenerateQRActivity::class.java)
                    startActivity(intent)
                }
                R.id.signout -> {
                    logoutUser()
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

        findViewById<FloatingActionButton>(R.id.fabAddRecord).setOnClickListener {
            val intent = Intent(this@MainActivity, AddProblemActivity::class.java)
            startActivity(intent)
        }

        // Check if the user is logged in
        checkIfUserIsLoggedIn()
    }



    private fun checkIfUserIsLoggedIn() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userID: String? = sharedPreferences.getString("USER_ID", null)

        if (userID.isNullOrEmpty()) {
            // No userID found in shared preferences, redirect to LoginActivity
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun logoutUser() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("USER_ID")
        editor.apply()

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
