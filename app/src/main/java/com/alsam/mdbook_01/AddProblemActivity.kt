package com.alsam.mdbook_01

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddProblemActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val collectionReference = db.collection("problems")

    private lateinit var addTitleEditText: EditText
    private lateinit var addDescriptionEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)

        addTitleEditText = findViewById(R.id.addTitle)
        addDescriptionEditText = findViewById(R.id.addDescription)

        val saveButton: Button = findViewById(R.id.saveButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)
        val addDateButton: Button = findViewById(R.id.addDateBtn)

        // Setting onClickListener for Save Button
        saveButton.setOnClickListener {
            val title = addTitleEditText.text.toString().trim()
            val description = addDescriptionEditText.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                showDatePicker(title, description)
            } else {
                showMessage("Please fill in both title and description.")
            }
        }
        addDateButton.setOnClickListener {
            val title = addTitleEditText.text.toString().trim()
            val description = addDescriptionEditText.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                showDatePicker(title, description)
            } else {
                showMessage("Please fill in both title and description.")
            }

        }

        // Setting onClickListener for Cancel Button
        cancelButton.setOnClickListener {
            showMessage("Operation Cancelled")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker(title: String, description: String) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"

                // Create a HashMap to store problem data
                val problemData = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "date" to selectedDate
                )

                // Add data to Firestore collection
                collectionReference.add(problemData)
                    .addOnSuccessListener { documentReference ->
                        showMessage("Problem added successfully with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        showMessage("Error adding problem: $e")
                    }




                val itemRecordIntent = Intent(this@AddProblemActivity, ItemRecord::class.java)
                itemRecordIntent.putExtra("SELECTED_DATE", selectedDate)
                startActivity(itemRecordIntent)

                val mainActivityIntent = Intent(this@AddProblemActivity, MainActivity::class.java)
                mainActivityIntent.putExtra("SELECTED_DATE", selectedDate)
                mainActivityIntent.putExtra("TITLE", title)
                mainActivityIntent.putExtra("DESCRIPTION", description)
                startActivity(mainActivityIntent)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }


}
