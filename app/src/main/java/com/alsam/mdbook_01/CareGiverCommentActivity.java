package com.alsam.mdbook_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CareGiverCommentActivity extends AppCompatActivity {


    Button addCommentBtn;
    EditText etAddComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_care_giver_comment);

        etAddComment = findViewById(R.id.etAddComment);

        Intent intent = getIntent();
     String id=   intent.getStringExtra("id");
        addCommentBtn = findViewById(R.id.addCommentBtn);
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              String comment =  etAddComment.getText().toString();
              if(comment!=null && comment.length()>0)
              {
                  updateSpecicRecord( id,comment) ;


              }
              else{

                  Toast.makeText(CareGiverCommentActivity.this,"Please add comment first",Toast.LENGTH_LONG).show();

              }





            }
        });
    }

    private void updateSpecicRecord(String id,String comment) {



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionPath = "records";
        String documentId = id; // Replace with the actual document ID

// Create a reference to the document
        DocumentReference documentRef = db.collection(collectionPath).document(documentId);

// Create a map with the fields you want to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("comment", comment);


// Update the document
        documentRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document updated successfully
                        Toast.makeText(CareGiverCommentActivity.this,"Comment Updated Successfully",Toast.LENGTH_LONG).show();

                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors

                        Toast.makeText(CareGiverCommentActivity.this,"Something went wrong please try again later",Toast.LENGTH_LONG).show();
                    }
                });


    }
}