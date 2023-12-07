package com.alsam.mdbook_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class AddPatient extends AppCompatActivity {

    EditText enterUserText;
    Button cancelBtn,readQR,addPatientBtn;

    String enteredUserId ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        enterUserText = findViewById(R.id.enterUserText);
        cancelBtn = findViewById(R.id.cancelBtn);
        readQR = findViewById(R.id.readQR);
        addPatientBtn= findViewById(R.id.addPatientBtn);
        readQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        IntentIntegrator integrator = new IntentIntegrator(AddPatient.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0) ;// Use the device's default camera
        integrator.setOrientationLocked(false) ;// Unlock orientation (optional)
        integrator.initiateScan();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterUserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                enteredUserId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enteredUserId!=null && enteredUserId.length()>0)
                {

                    searchUser(enteredUserId);



                }
                else{

                    Toast.makeText(AddPatient.this,"Please enter User/patientId First",Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private void searchUser(String scannedData) {





        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(scannedData)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            adPatient(scannedData);
                        } else {
                            Toast.makeText(this, "No Patient Found", Toast.LENGTH_SHORT).show();
                            // Redirect to the appropriate activity as needed if the user ID is not found

                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Redirect to the appropriate activity in case of an error

                    });












        }
    }

    private  void adPatient(String patientId)
    {



        SharedPreferences sharedPreferences  = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
       String   CareGiverId = sharedPreferences.getString("USER_ID","");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("patients");

// Create a data map for your document
        Map<String, Object> data = new HashMap<>();
        data.put("patientId", patientId);
        data.put("caregiverId", CareGiverId);
// Add other fields as needed

// Add a new document with a generated ID
        collectionReference
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Document added successfully
                        String newDocumentId = documentReference.getId();

                        redirectToPatientProblemList(patientId);


                        // You can perform additional actions here if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        Toast.makeText(AddPatient.this,"Something went wrong please try again later",Toast.LENGTH_LONG).show();
                        // Handle errors here
                    }
                });


    }

    private void redirectToPatientProblemList(String patientId) {




        Intent intent1 = new Intent(AddPatient.this, PatientProblemList.class);

        intent1.putExtra("USER_ID", patientId);

        startActivity(intent1);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents(); // Retrieve the scanned data

                // Check if scannedData matches a user ID in the database
                searchUser(scannedData);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}