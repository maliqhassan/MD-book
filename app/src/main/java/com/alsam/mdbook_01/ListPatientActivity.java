package com.alsam.mdbook_01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class ListPatientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> patientIDs;
    private RecyclerView recyclerView;
    private PatientAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    private Caregiver caregiver;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);

//        Toolbar toolbar = findViewById(R.id.toolbar);
        initViews();
        setupNavigationDrawerAndToolbar();
        fab = findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(ListPatientActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a QR Code");
                integrator.setCameraId(0); // Use the device's default camera
                integrator.setOrientationLocked(false); // Unlock orientation (optional)
                integrator.initiateScan();


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

    private void setupNavigationDrawerAndToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_small_drawer_icon);

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

}
