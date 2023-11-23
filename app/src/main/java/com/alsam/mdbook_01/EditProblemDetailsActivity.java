package com.alsam.mdbook_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

/*
 * EditProblemDetailActivity
 *
 * Version 0.0.1
 *
 * 2018-11-18
 *
 * Copyright (c) 2018. All rights reserved.
 */



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;


import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a view for editing the problems details
 *

 *
 * @author Noah Burghardt
 * @author Raj Kapadia
 * @author Vanessa Peng
 * @author James Aina
 *
 * @version 0.0.1
 **/
public class EditProblemDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTitle;
    private Button editDate;
    private EditText editDescription;
    private Button save;
    private Button cancel;
    private Problem problem;
    private int problemPos;
    private Patient patient;
    private Date date;
    private String strDate;

    SimpleDateFormat dateFormat;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_problem_details);

        dateFormat  = new SimpleDateFormat("dd/MM/yy");

        // set the views and buttons appropriately by id's
        editTitle = findViewById(R.id.showTitle);
        editDate = findViewById(R.id.datePickerEdit);
        editDescription = findViewById(R.id.editDescription);
        save = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        Intent intent = getIntent();
      id =  intent.getStringExtra("id");


//        UserManager.initManager();

        loadProblemData(id);

        // Switches to the main activity upon the click of the save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 updateData();

//                userManager.saveUser(patient);
              //  backToMainPage();
            }
        });
        // Switches to the main activity upon the click of the cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

//                backToMainPage();
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);


            }
        });
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void updateData() {


        {

            String  title = editTitle.getText().toString();
            String desc =editDescription.getText().toString();

            // Reference to the document
            DocumentReference documentRef = db.collection("problems").document(id);

            // Create a Map with the updated data
            Map<String, Object> updates = new HashMap<>();
            updates.put("date", strDate);
            updates.put("description", desc);
            updates.put("title", title);
            updates.put("userid", userId);

            // Update the document
            documentRef.update(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Document updated successfully

                                finish();
                                System.out.println("Document updated successfully");
                            } else {
                                // Handle the error
                                Exception e = task.getException();
                                if (e != null) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    String userId;
    private void loadProblemData(String id ) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Reference to the document
        DocumentReference documentRef = db.collection("problems").document(id);

// Retrieve the data
        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve the data
                     strDate = documentSnapshot.getString("date");
                    String description = documentSnapshot.getString("description");
                    String title = documentSnapshot.getString("title");
                    userId   = documentSnapshot.getString("userid");


//                    problemPos = getIntent().getExtras().getInt("problemPos");
//                    problem = patient.getProblems().get(problemPos);
                    editTitle.setText(title);
                    editDescription.setText(description);




                    editDate.setText(strDate);


                    // Now you can use the retrieved data as needed
                    // For example, display it in your UI or perform other actions
                } else {
                    // Document does not exist
                }
            }
        });







    }

    /**
     * Creates a DatePicker instance and lets the user select the date from
     * calender that pops. This happens when
     * @param v
     */
    public void showDatePicker(View v) {
//        DialogFragment newFragment = new AddProblemActivity().showDatePicker("","");
//        newFragment.show(getSupportFragmentManager(), "date picker");
    }


    /**
     * Creates a new intent for switching to the ListProblemActivity
     */
    public void backToMainPage(){
        Intent intent = new Intent(EditProblemDetailsActivity.this, ListProblemActivity.class);
        startActivity(intent);
        this.finish();
    }

    private String setDate(Date date) {
        String strDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        strDate = dateFormat.format(date);
        return strDate;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        view.setMaxDate(Calendar.getInstance().getTimeInMillis());
        date = new Date(view.getYear(), view.getMonth(), view.getDayOfMonth());
        String strDate = setDate(date);
        editDate.setText(strDate);
        problem.setDate(date);
        Toast.makeText(EditProblemDetailsActivity.this, strDate, Toast.LENGTH_LONG).show();
    }

    /**
     * A static fragment class required for proper DatePicker implementation
     */
    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }
    }

}

