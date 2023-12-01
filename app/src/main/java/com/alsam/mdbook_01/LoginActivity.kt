package com.alsam.mdbook_01

import android.app.Activity
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

    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUserID = findViewById<EditText>(R.id.etUserID)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton  = findViewById<Button>(R.id.registerBtn)


        registerButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)

            // Optionally, you can add extra data to the intent
            intent.putExtra("key", "value")

            // Start the activity
            startActivity(intent)

        }

        loginButton.setOnClickListener {
            userID = etUserID.text.toString().trim()
            if (userID.isNotEmpty()) {
                // Store userID in SharedPreferences
                saveUserIDToSharedPreferences(userID)

                // Check if the user is a caregiver (isChecked)
                checkIfCaregiver(userID)
            } else {
                Toast.makeText(this, "Please enter User ID", Toast.LENGTH_SHORT).show()
            }
        }


        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId: String? = sharedPreferences.getString("USER_ID", "")

        if (!userId.isNullOrEmpty()) {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
            // The userId is not empty or null
            // Your code here
        } else {
            // The userId is either empty or null
            // Handle the case where the userId is not available
        }

        userID = sharedPreferences.getString("USER_ID", null) ?: ""
    }

    private fun checkIfCaregiver(userID: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val isChecked = documentSnapshot.getBoolean("isChecked") ?: false

                    if (isChecked) {
                        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString( "userType", "caregiver")
                        editor.putString("USER_ID",userID)
                        editor.apply()


                        // If the user is a caregiver, open the QR code scanner
                        openCameraForScan()
                    } else {
                        // If not a caregiver, redirect to the desired activity (MainActivity, PatientProblemList, etc.)
                        redirectToAppropriateActivity(userID)
                    }
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openCameraForScan() {


        val intent = Intent(this, ListPatientActivity::class.java)

        startActivity(intent)
        finish()
//        val intent = Intent(this, ListPatientActivity::class.java)
////        intent.putExtra("USER_ID", userId)
//        startActivity(intent)
//        finish()

//        val integrator = IntentIntegrator(this)
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
//        integrator.setPrompt("Scan a QR Code")
//        integrator.setCameraId(0) // Use the device's default camera
//        integrator.setOrientationLocked(false) // Unlock orientation (optional)
//        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedData: String = result.contents // Retrieve the scanned data

                // Check if scannedData matches a user ID in the database
                verifyScannedUserID(scannedData)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun verifyScannedUserID(scannedData: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(scannedData)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    redirectToPatientProblemList(scannedData)
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                    // Redirect to the appropriate activity as needed if the user ID is not found
                    redirectToAppropriateActivity(userID)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                // Redirect to the appropriate activity in case of an error
                redirectToAppropriateActivity(userID)
            }
    }

    private fun redirectToPatientProblemList(userID: String) {
        // Redirect to PatientProblemList activity with the verified userID
        val intent = Intent(this, PatientProblemList::class.java)
        intent.putExtra("USER_ID", userID)
        startActivity(intent)
        finish() // Finish LoginActivity to prevent returning back here on back press
    }

    private fun redirectToAppropriateActivity(userID: String) {
        // Here, determine which activity to open based on your logic
        // For example, opening MainActivity or PatientProblemList
        // You can use the Intent to pass the userID if needed
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ID", userID)
        startActivity(intent)
        finish() // Finish LoginActivity to prevent returning back here on back press
    }

    private fun saveUserIDToSharedPreferences(userID: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("USER_ID", userID)
        editor.apply()
    }
}
