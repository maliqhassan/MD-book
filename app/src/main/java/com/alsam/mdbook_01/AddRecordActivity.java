package com.alsam.mdbook_01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import android.media.Image;

/**
 * Creates an activity for the user to add a record
 * Displays a list of all the problems already added
 *

 * @author ThomasChan
 * @author Jayanta Chatterjee
 * @author Raj Kapadia
 *
 * @version 3.0.0
 */

public class AddRecordActivity extends AppCompatActivity {
    private static final String TAG = "AddRecordActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final Integer MAP_ACTIVITY_REQUEST_CODE = 0;
    private static final Integer BODY_ACTIVITY_REQUEST_CODE = 5;
    // Initialize all the required imageViews ans Buttons

    private ArrayList<Record> recordList;
    private Record record;
    private Double Lat;
    private Double Long;
    private String Title;
    private String problemPos;
    private Date recordDate;
    private ImageView image;
    private EditText headline;
    private EditText Description;
    private Button geo;
    private Button body;
    private Button save;
    private Button cancel;
    private ArrayList<BodyLocation> bodylocationlist = new ArrayList<BodyLocation>();
    String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        // set the view and butons appropriately by id's
        headline = findViewById(R.id.headline);

        Description = findViewById(R.id.description);
        geo = findViewById(R.id.geo);
        body = findViewById(R.id.body);
        image = findViewById(R.id.addImage);
        save = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);

        Intent intent = getIntent();
       id = intent.getStringExtra("id");

        createDocument(id);
image.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {



        openGallery();
    }
});


//        UserManager.initManager();
//        final UserManager userManager = UserManager.getManager();
        recordList = new ArrayList<>();
        final Patient patient = (Patient) UserController.getController().getUser();
        problemPos = getIntent().getExtras().getString("id");
        /*
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddRecordActivity.this, "Add photo",
                        Toast.LENGTH_LONG).show();
            }
        });*/

        // Switches to addBodyLocationActivity upon the click of the body button
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddBodyLoc();
            }
        });

        // Switches to addBodyLocationActivity upon the click of the save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{


                String hd =    headline.getText().toString();
                 String hs =   Description.getText().toString();

                     if(selectedImageUri!=null )
                     {

                         FirebaseStorage storage = FirebaseStorage.getInstance();
                         StorageReference storageRef = storage.getReference();

// Create a reference to the location where you want to store the file (replace "images" with your desired path)
                         StorageReference imagesRef = storageRef.child("images");

// Get the Uri of the image you want to upload
                         Uri fileUri = selectedImageUri;// Uri of your image

// Create a reference to the file you want to upload


// Get the Uri of the image you want to upload
                        // Uri of your image

// Create a reference to the file you want to upload
                         final StorageReference imageRef = imagesRef.child("image.jpg");


// Upload the file to Firebase Storage
                         imageRef.putFile(fileUri)
                                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                         // File successfully uploaded
                                         Log.d(TAG, "File uploaded: " + taskSnapshot.getMetadata().getPath());

                                         // Get the download URL of the uploaded file (optional)
                                         imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                             @Override
                                             public void onSuccess(Uri downloadUrl) {
                                                String imageUrl = downloadUrl.toString();



                                                 {

                                                     FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a new document without specifying a document ID
                                                     DocumentReference collectionRef = db.collection("records").document(documentId);

// Create a data object with the field "userid" set to "3"
                                                     Map<String, Object> data = new HashMap<>();
                                                     data.put("image", imageUrl);
                                                     data.put("headline",hd);
                                                     data.put("title",hs);

// Add the data to a new document with an automatically generated ID
                                                     collectionRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                         @Override
                                                         public void onSuccess(Void unused) {

                                                             finish();
                                                         }
                                                     });

                                                 }





                                             }
                                         });
                                     }
                                 })
                                 .addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         // Handle unsuccessful uploads
                                         Log.e(TAG, "Error uploading file", e);
                                     }
                                 });


                     }

//                    recordDate = new Date();
//                    if (record == null) {
//                        record = new Record(headline.getText().toString(), recordDate,
//                        "");
//                        record.setComment(Description.getText().toString());
//
//                    }
//                    if (Lat != null){
//                        if (Long != null){
//                            if (Title != null){
//                                new GeoLocation(Lat, Long, Title)
//
//
//                            }
//                        }
//                    }
//                    if(bodylocationlist != null){
//                        for(int i = 0; i<bodylocationlist.size(); i++) {
//                            record.setBodyLocation(bodylocationlist.get(i));
//                        }
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                        "bodylocation(s) added to record", Toast.LENGTH_SHORT);
//                        toast.show();
//
//                    }
//                   // patient.getProblems().get(problemPos).addRecord(record);
//                  //  userManager.saveUser(patient);
//                    Toast.makeText(AddRecordActivity.this
//                        ,"Record " + headline.getText().toString() + " Added"
//                        ,Toast.LENGTH_SHORT).show();
//                    setResult(RESULT_OK);
                    finish();
                }catch (NullPointerException e){
                    Toast.makeText(AddRecordActivity.this, "No Title Entered",
                        Toast.LENGTH_SHORT).show();
                }catch (IllegalArgumentException e) {
                    Toast.makeText(AddRecordActivity.this, "Illegal Arguments in Title",
                        Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Switches to AddProblemsActivity upon the click of the cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endActivity();
            }
        });

        // Switches to AddProblemsActivity upon the click of the geolocation button
        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeoLoc();
            }
        });


    }


    String  documentId;
    private void createDocument(String id) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a new document without specifying a document ID
        CollectionReference collectionRef = db.collection("records");

// Create a data object with the field "userid" set to "3"
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

// Add the data to a new document with an automatically generated ID
        collectionRef.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Document successfully written with an automatically generated ID
                        documentId =documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                        Log.e(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * After an Intent is finished it will get the results of the intent
     * @param requestCode Request code for intents
     * @param resultCode Result code to see if activities finished
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Lat = (Double) data.getSerializableExtra("Lat");
                Long = (Double) data.getSerializableExtra("Long");
                Title = (String) data.getSerializableExtra("Title");
            }
        }
        if(requestCode == BODY_ACTIVITY_REQUEST_CODE){
            if(resultCode != RESULT_CANCELED && data != null){
                BodyLocation bodylocation = (BodyLocation) data.getSerializableExtra(
                        "bodylocation");
                bodylocationlist.add(bodylocation);
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri  = data.getData();

            // Display the selected image in an ImageView
            image.setImageURI(selectedImageUri);
        }
    }

    Uri selectedImageUri;

    /**
     * Creates a new intent for switch to the AddBodyLocationActivity
     */
    public void goAddBodyLoc(){
        Intent addRecordPage = new Intent(this, NewBodyLocationView.class);
        startActivityForResult(addRecordPage, BODY_ACTIVITY_REQUEST_CODE);

    }

    /**
     * Creates a new intent for switch to the ListProblemActivity
     */
    public void endActivity(){
        this.finish();
    }
    /**
     * Creates a new intent for switch to the ViewLocationActivity
     */
    public void openGeoLoc(){
        Intent launchmap= new Intent(this, MapActivity.class);
        startActivityForResult(launchmap, MAP_ACTIVITY_REQUEST_CODE);
    }


    /**
     * Checks to see if Google Services are working fine for proper map functionality
     * @return boolean value
     */
    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking Google Services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddRecordActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //Everything is fine and user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //Error occured but is fixable
            Log.d(TAG,"isServicesOK: an error has occured but is fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddRecordActivity
                .this,available , ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You cant make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    // Function to open the gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handling the result from the gallery selection

}
