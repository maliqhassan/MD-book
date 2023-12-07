package com.alsam.mdbook_01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListPatientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> patientIDs;
    private RecyclerView recyclerView;
    private PatientAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    private Caregiver caregiver;
    private FloatingActionButton fab;

    String CareGiverId;

    CardAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);

        SharedPreferences sharedPreferences  = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        CareGiverId = sharedPreferences.getString("USER_ID","");
        recyclerView = findViewById(R.id.cardRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ListPatientActivity.this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

//        Toolbar toolbar = findViewById(R.id.toolbar);
        initViews();
        setupNavigationDrawerAndToolbar();
        fab = findViewById(R.id.fabAddRecord);


        adapter = new CardAdapter1(cardList,new CardClickListener() {
            @Override
            public void onClick(int position) {
                // Handle item click
                // You can add your logic here
//                Intent intent = new Intent(ListPatientActivity.this, ListProblemActivity.class);
//                intent.putExtra("id", problemList.get(position).getTitle());
//                startActivity(intent);

                if(cardList!=null)
                {  cardList.get(position);
                    Intent intent = new Intent(ListPatientActivity.this, PatientProblemList.class);

                    intent.putExtra("USER_ID", userID);
                    startActivity(intent);
                    finish();

                }
                else{
                 String tilr =   problemList.get(position).getTitle();
                    Intent intent = new Intent(ListPatientActivity.this, PatientProblemList.class);

                    intent.putExtra("USER_ID", tilr);
                    startActivity(intent);
                    finish();
                }

            }
        });

        loadData(CareGiverId);

        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(ListPatientActivity.this,AddPatient.class);
                startActivity(intent);


//
//                IntentIntegrator integrator = new IntentIntegrator(ListPatientActivity.this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//                integrator.setPrompt("Scan a QR Code");
//                integrator.setCameraId(0); // Use the device's default camera
//                integrator.setOrientationLocked(false); // Unlock orientation (optional)
//                integrator.initiateScan();


            }
        });

//        UserManager.initManager();
//        UserManager userManager = UserManager.getManager();
//
//        caregiver = (Caregiver) UserController.getController().getUser();
//        patientIDs = caregiver.getPatientList();
//
//
//
//        /* Create recycler view */
//        recyclerView = findViewById(R.id.recylerView);
//        recyclerView.setHasFixedSize(true);
//        mLayoutmanager = new LinearLayoutManager(this);
//        mAdapter = new PatientAdapter(patientIDs);
//        recyclerView.setLayoutManager(mLayoutmanager);
//        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    private void initViews() {
        cardRecyclerView = findViewById(R.id.cardRecyclerView);
//        cardRecyclerView.layoutManager = LinearLayoutManager(this)
//        cardList = ArrayList()

//        val placeholderClick: (Int) -> Unit = { position ->
//                // Placeholder onItemClick function, it will be replaced later
//                // Implement the actual behavior in LoadData() method
//        }
//
//        cardAdapter = CardAdapter(cardList, placeholderClick)
        cardRecyclerView.setAdapter( cardAdapter);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.care_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            SharedPreferences sharedPreferences= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor  editor = sharedPreferences.edit();
            editor.remove("USER_ID");
            editor.remove("userType");
            editor.apply();

            Intent intent  = new Intent(ListPatientActivity.this,LoginActivity.class);
            startActivity(intent);
          //  Toast.makeText(ListPatientActivity.this,"Lofout",Toast.LENGTH_LONG).show();
            // Handle logout action here
            // You can add code to sign out the user or perform any other logout-related actions
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupNavigationDrawerAndToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_small_drawer_icon);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        // Handle profile item click
                        break;

                    case R.id.signout:
                        SharedPreferences sharedPreferences= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                         SharedPreferences.Editor  editor = sharedPreferences.edit();
                        editor.remove("USER_ID");
                        editor.remove("userType");
                        editor.apply();

                       Intent intent  = new Intent(ListPatientActivity.this,LoginActivity.class);
                       startActivity(intent);
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private   ArrayList<CardItem>  cardList;
    private  RecyclerView   cardRecyclerView;
    private   CardAdapter  cardAdapter;

    private  DrawerLayout drawerLayout;
    private  NavigationView  navigationView;
    private  String   userID;
    private   String userType;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents(); // Retrieve the scanned data

                // Check if scannedData matches a user ID in the database
                verifyScannedUserID(scannedData);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void verifyScannedUserID(String scannedData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(scannedData)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                      adPatient(scannedData);
                    } else {
                        Toast.makeText(this, "No Patient Found", Toast.LENGTH_SHORT).show();
                        // Redirect to the appropriate activity as needed if the user ID is not found
                        redirectToAppropriateActivity(scannedData);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Redirect to the appropriate activity in case of an error
                    redirectToAppropriateActivity(scannedData);
                });


        adapter = new CardAdapter1(cardList,new CardClickListener() {
            @Override
            public void onClick(int position) {
                // Handle item click
                // You can add your logic here
                Intent intent = new Intent(ListPatientActivity.this, OptionMenuActivity.class);
                intent.putExtra("id", problemList.get(position).getId());
                startActivity(intent);


            }
        });









    }

    public interface CardClickListener {
        void onClick(int position);
    }

    ArrayList <CardItem>  problemList = new ArrayList();
    private  void loadData(String userID)
    {

        SharedPreferences sharedPreferences  = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        CareGiverId = sharedPreferences.getString("USER_ID","");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference patientsCollection = db.collection("patients");

        patientsCollection.whereEqualTo("caregiverId", CareGiverId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documents) {
                        for (QueryDocumentSnapshot document : documents) {
                            // Access each document here
                            String patientId = document.getString("patientId");




                                CardItem problem = new CardItem(patientId, "", "", document.getId());
                                problemList.add(problem);
                            }

                            if (!problemList.isEmpty())
                            {
                                adapter.setDataList(problemList);
                                adapter.notifyDataSetChanged();
                                // Update RecyclerView data here
                            } else
                            {
                                // Display a message if no problems are found for the user
                                // You can use Toast or any other way to display this message
                            }
                        }

                            // Access other fields as needed

                            // Perform actions with the document data


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failures if any
                    }
                });

    }


    private  void adPatient(String patientId)
    {



        SharedPreferences sharedPreferences  = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        CareGiverId = sharedPreferences.getString("USER_ID","");
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


                        Toast.makeText(ListPatientActivity.this,"Something went wrong please try again later",Toast.LENGTH_LONG).show();
                        // Handle errors here
                    }
                });


    }





    private void redirectToPatientProblemList(String userID) {
        // Redirect to PatientProblemList activity with the verified userID
        Intent intent = new Intent(this, PatientProblemList.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);
        finish(); // Finish LoginActivity to prevent returning back here on back press
    }

    private void redirectToAppropriateActivity(String userID) {
        // Here, determine which activity to open based on your logic
        // For example, opening MainActivity or PatientProblemList
        // You can use the Intent to pass the userID if needed
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);
        finish(); // Finish LoginActivity to prevent returning back here on back press
    }

    public interface PlaceholderClickListener {
        void onItemClick(int position);
    }

}

