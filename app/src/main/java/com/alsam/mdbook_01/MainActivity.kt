package com.alsam.mdbook_01

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    private lateinit var cardList: ArrayList<CardItem>
    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing views and adapters
        initViews()

        // Setting up navigation drawer and toolbar
        setupNavigationDrawerAndToolbar()

        // FloatingActionButton click listener to open AddProblemActivity
        findViewById<FloatingActionButton>(R.id.fabAddRecord).setOnClickListener {
            startActivity(Intent(this@MainActivity, AddProblemActivity::class.java))
        }

        // Check if the user is logged in
        checkIfUserIsLoggedIn()
    }

    override fun onResume() {

        LoadData(userID)
        super.onResume()
    }

    private fun initViews() {
        cardRecyclerView = findViewById(R.id.cardRecyclerView)
        cardRecyclerView.layoutManager = LinearLayoutManager(this)
        cardList = ArrayList()

        val placeholderClick: (Int) -> Unit = { position ->
            // Placeholder onItemClick function, it will be replaced later
            // Implement the actual behavior in LoadData() method
        }

        cardAdapter = CardAdapter(cardList, placeholderClick)
        cardRecyclerView.adapter = cardAdapter
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
    }

    private fun setupNavigationDrawerAndToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_small_drawer_icon)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    // Handle profile item click
                }
                R.id.shareQR -> {
                    startActivity(Intent(this@MainActivity, GenerateQRActivity::class.java))
                }
                R.id.signout -> {
                    logoutUser()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun LoadData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val problemsCollection = db.collection("problems")
        val query: Query = problemsCollection.whereEqualTo("userid", userId)

        query.get()
            .addOnSuccessListener { result ->
                val cardItemList = mutableListOf<CardItem>()
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val date = document.getString("date") ?: ""
                    val  id = document.id;

                    val cardItem = CardItem(title, description, date,id)
                    cardItemList.add(cardItem)
                }

                cardAdapter = CardAdapter(cardItemList) { position ->
                    val intent = Intent(this@MainActivity, OptionMenuActivity::class.java)
                    intent.putExtra("id", cardItemList.get(position).id)
                    startActivity(intent)
                }
                cardRecyclerView.adapter = cardAdapter
            }
            .addOnFailureListener { exception ->
                showToast("Failed to load data: ${exception.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkIfUserIsLoggedIn() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("USER_ID", null) ?: ""

        if (userID.isEmpty()) {
            redirectToLogin()
        } else {
            LoadData(userID)
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun logoutUser() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("USER_ID")
        editor.apply()

        redirectToLogin()
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
