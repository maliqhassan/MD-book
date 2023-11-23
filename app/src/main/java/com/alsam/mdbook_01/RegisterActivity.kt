package com.alsam.mdbook_01

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUserID = findViewById<EditText>(R.id.etUserIDR)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val cgCheckBox = findViewById<CheckBox>(R.id.cgCheckBox)

        registerButton.setOnClickListener {
            val userID = etUserID.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()
            val isChecked = cgCheckBox.isChecked // Get the state of the checkbox

            if (userID.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty()) {
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Loading...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                checkUserExists(userID, email, phoneNumber, isChecked)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserExists(userID: String, email: String, phoneNumber: String, isChecked: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        // Check if the userID or email already exist in the Firestore database
        usersCollection.whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { userIDResult ->
                if (!userIDResult.isEmpty) {
                    progressDialog.dismiss()
                    Toast.makeText(this, "User ID already exists", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                usersCollection.whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { emailResult ->
                        if (!emailResult.isEmpty) {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        // If neither userID nor email exist, proceed to register the user
                        registerUser(userID, email, phoneNumber, isChecked)
                    }
                    .addOnFailureListener { exception ->
                        progressDialog.dismiss()
                        Toast.makeText(this, "Error checking email: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error checking userID: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerUser(userID: String, email: String, phoneNumber: String, isChecked: Boolean) {
        val user = hashMapOf(
            "userID" to userID,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "isChecked" to isChecked // Add the isChecked value to the user data
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                finish();
                // Redirect to the next screen or perform necessary actions upon successful registration
            }
            .addOnFailureListener { exception ->

                Toast.makeText(this, "Registration failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
