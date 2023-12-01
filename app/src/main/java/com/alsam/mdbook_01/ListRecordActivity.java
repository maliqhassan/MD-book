package com.alsam.mdbook_01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListRecordActivity extends AppCompatActivity {

    private static final String TAG = "ListRecordActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private RecyclerView mRecyclerView;
    private RecordAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Record> recordList = new ArrayList<>();
    private Integer problemPos;
    private static final Integer ADD_RECORD_REQUEST_CODE = 0;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);


        Intent  intent = getIntent();

      id =  intent.getStringExtra("id");





//        Patient patient = (Patient) UserController.getController().getUser();
//        if (problemPos == null) {
//            problemPos = getIntent().getExtras().getInt("problemPos");
//        }
//        recordList = patient.getProblems().get(problemPos).getRecords();
//        Collections.sort(recordList, new Comparator<Record>() {
//            @Override
//            public int compare(Record p, Record q) {
//                {
//                    if (p.getDate().before(q.getDate())) {
//                        return 11;
//                    } else if (p.getDate().after(q.getDate())) {
//                        return -1;
//                    } else {
//                        return 0;
//                    }
//                }
//            }
//        });



        mRecyclerView = findViewById(R.id.recordRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecordAdapter(recordList, ListRecordActivity.this, new RecordAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent addRecord = new Intent(ListRecordActivity.this, AddRecordActivity.class);
                addRecord.putExtra("id", id);
                addRecord.putExtra("headline",recordList.get(position).getTitle());
                addRecord.putExtra("description",recordList.get(position).getDescription());
                addRecord.putExtra("image",recordList.get(position).getPhotos());
                addRecord.putExtra("frontPic",recordList.get(position).getFrontPic());
                addRecord.putExtra("backPic",recordList.get(position).getBackPic());
                addRecord.putExtra("lat",recordList.get(position).getGeoLocation().getLat());
                addRecord.putExtra("lng",recordList.get(position).getGeoLocation().getLong());
                startActivity(addRecord);

            }

            @Override
            public void viewmapClick(int postion) {

               int i = postion;



            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter.setOnItemClickListener(new RecordAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                recordList.get(position);
                Intent addRecord = new Intent(ListRecordActivity.this, AddRecordActivity.class);
                 addRecord.putExtra("id", id);
                 addRecord.putExtra("headline",recordList.get(position).getTitle());
                 addRecord.putExtra("description",recordList.get(position).getDescription());
                 addRecord.putExtra("image",recordList.get(position).getPhotos());
                 addRecord.putExtra("frontPic",recordList.get(position).getFrontPic());
                addRecord.putExtra("backPic",recordList.get(position).getBackPic());
                addRecord.putExtra("lat",recordList.get(position).getGeoLocation().getLat());
                addRecord.putExtra("lng",recordList.get(position).getGeoLocation().getLong());
                startActivity(addRecord);

                //Toast.makeText(ListRecordActivity.this, "works",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void viewmapClick(int postion) {
                if (isServicesOK())
                {
                    Intent launchmap = new Intent(ListRecordActivity.this, ViewMapActivity.class);
                    if (recordList.get(postion).getLocation() != null){
                        launchmap.putExtra("recieveLat",recordList.get(postion).getLocation().getLat());
                        launchmap.putExtra("recieveLong",recordList.get(postion).getLocation().getLong());
                        launchmap.putExtra("recieveTitle",recordList.get(postion).getLocation().getTitle());
                        startActivity(launchmap);
                    }else {
                        Toast.makeText(ListRecordActivity.this, "No location for record",Toast.LENGTH_SHORT).show();
                    }

                }

            }


        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRecord();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RECORD_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                UserManager.initManager();
                UserManager userManager = UserManager.getManager();
                Patient patient = (Patient) UserController.getController().getUser();
                recordList = patient.getProblems().get(problemPos).getRecords();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Changes to AddRecordActivity, when user clicks add
     */
    public void AddRecord(){
        Intent addRecord = new Intent(this, AddRecordActivity.class);
        addRecord.putExtra("id", id);
        startActivityForResult(addRecord, ADD_RECORD_REQUEST_CODE);
        //this.finish();
    }

    /**
     * Checks the google services for proper map functionality
     * @return bool
     */
    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking Google Services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(available == ConnectionResult.SUCCESS){
            //Everything is fine and user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //Error occured but is fixable
            Log.d(TAG,"isServicesOK: an error has occured but is fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available , ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You cant make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        loadData();
//        SyncController syncController = new SyncController();
//        registerReceiver(syncController,filter);
    }

    private void loadData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference problemsCollection = db.collection("records");
        Query query = problemsCollection.whereEqualTo("id", id);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot result) {
                       recordList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : result) {
                            String title = document.getString("headline") != null ? document.getString("headline") : "";
                            String description = document.getString("title") != null ? document.getString("title") : "";
                            String date = document.getString("date") != null ? document.getString("date") : "";
                            ArrayList<String> bodyLocations = (ArrayList<String>) document.get("bodyLocations");

                            String lat = document.getString("latitude") != null ? document.getString("latitude") : "";
                            String titleK = document.getString("titleK") != null ? document.getString("titleK") : "";
                            String lng = document.getString("longitude") != null ? document.getString("longitude") : "";

                           String  photos = document.getString("image") != null ? document.getString("image") : "";

                            GeoLocation geoLocation =null;
                           if(lat!=null && lng !=null && lat.length()>0)
                           {
                            geoLocation = new GeoLocation(Double.parseDouble(lat),Double.parseDouble(lng),titleK);
                           }


                            Record cardItem = new Record(title,"",description,photos,geoLocation) ;


                            if(title!=null && title.length()>0 && description!=null && description.length()>0)
                            {         recordList.add(cardItem);


                            }


                        }

                        mAdapter = new RecordAdapter(recordList, ListRecordActivity.this, new RecordAdapter.onItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                Intent addRecord = new Intent(ListRecordActivity.this, AddRecordActivity.class);
                                addRecord.putExtra("id", id);
                                addRecord.putExtra("headline",recordList.get(position).getTitle());
                                addRecord.putExtra("description",recordList.get(position).getDescription());
                                addRecord.putExtra("image",recordList.get(position).getPhotos());
                                addRecord.putExtra("frontPic",recordList.get(position).getFrontPic());
                                addRecord.putExtra("backPic",recordList.get(position).getBackPic());
                                try {
                                    addRecord.putExtra("lat",recordList.get(position).getGeoLocation().getLat());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    addRecord.putExtra("lng",recordList.get(position).getGeoLocation().getLong());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                startActivity(addRecord);

                            }

                            @Override
                            public void viewmapClick(int postion) {
                           GeoLocation  geoLocation =     recordList.get(postion).getGeoLocation();
                                if(geoLocation!=null)
                                {
                                    Intent launchmap = new Intent(ListRecordActivity.this, ViewMapActivity.class);
                                    if (recordList.get(postion).getLocation() != null){
                                        launchmap.putExtra("recieveLat",String.valueOf(recordList.get(postion).getLocation().getLat()));
                                        launchmap.putExtra("recieveLong",String.valueOf(recordList.get(postion).getLocation().getLong()));
                                        launchmap.putExtra("recieveTitle",String.valueOf(recordList.get(postion).getLocation().getTitle()));
                                        startActivity(launchmap);
                                    }else {
                                        Toast.makeText(ListRecordActivity.this, "No location for record",Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else{

                                    Toast.makeText(ListRecordActivity.this,"No Location Found",Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        mRecyclerView.setAdapter(mAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //showToast("Failed to load data: " + exception.getMessage());
                    }
                });




    }
}