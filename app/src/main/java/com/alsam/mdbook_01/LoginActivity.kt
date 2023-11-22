package com.alsam.mdbook_01

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class LoginActivity : AppCompatActivity() {

    private lateinit var userID: String // Variable to hold the userID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUserID = findViewById<EditText>(R.id.etUserID)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val scanQRBtn = findViewById<Button>(R.id.scanQRBtn)

        checkIfUserIsLoggedIn()

        loginButton.setOnClickListener {
            userID = etUserID.text.toString().trim()
            if (userID.isNotEmpty()) {
                // Store userID in SharedPreferences
                saveUserIDToSharedPreferences(userID)

                checkDatabaseForUserID(userID)
            } else {
                Toast.makeText(this, "Please enter User ID", Toast.LENGTH_SHORT).show()
            }
        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        scanQRBtn.setOnClickListener {
            openCameraForScan()
        }
    }
    private fun checkIfUserIsLoggedIn() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userID: String? = sharedPreferences.getString("USER_ID", null)

        if (!userID.isNullOrEmpty()) {
            // User is already logged in, check database and redirect accordingly
            checkDatabaseForUserID(userID)
        }
        // If userID does not exist in shared preferences, the user is not logged in and stays on LoginActivity
    }

    private fun openCameraForScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.setCameraId(0) // Use the device's default camera
        integrator.setOrientationLocked(false) // Unlock orientation (optional)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedData: String = result.contents // Retrieve the scanned data
                // Handle the scanned QR code data as needed
                Toast.makeText(this, "Scanned data: $scannedData", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkDatabaseForUserID(userID: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val isChecked = documentSnapshot.getBoolean("isChecked") ?: false
                    if (isChecked) {
                        startActivity(Intent(this, PatientProblemList::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    // Always pass the userID to GenerateQRActivity
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserIDToSharedPreferences(userID: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("USER_ID", userID)
        editor.apply()
    }

    private fun getUserIDFromSharedPreferences(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }
}
