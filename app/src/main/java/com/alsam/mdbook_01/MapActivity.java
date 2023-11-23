package com.alsam.mdbook_01;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    String showData;
    String id ;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;

    /* Widgets */
    private EditText mSearchText;
    private ImageView mGps;
    private RelativeLayout mAddLocation;



    /* Vars */
    private SupportMapFragment mapFragment;
    private Address address;
    private List<Address> myAddress = new ArrayList<>();
    private Patient patient;
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private UserManager userManager;

    boolean show=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        UserManager.initManager();
//        userManager = UserManager.getManager();

        mSearchText = (EditText) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.gpscenter);
        mAddLocation = (RelativeLayout) findViewById(R.id.add);
        Intent intent = getIntent();


        id=  intent.getStringExtra("id");

        if(intent.hasExtra("showData"))
        {
            show  = true;
            getLocationPermission();
        }
        else {
            show =false;
        }



      //  patient = (Patient) UserController.getController().getUser();
        mAddLocation.setVisibility(View.GONE);
   loadLocation();

    }

    private void loadLocation() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Replace "locations" with the name of your Firestore collection
// Create a query to retrieve documents from the collection
        Query query = db.collection("locations").whereEqualTo("problemId",id);

// Retrieve the documents
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // The query was successful, and we have a QuerySnapshot
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Access the data from each document
                        String latitude = document.getString("latitude");
                        String longitude = document.getString("longitude");
                        String title = document.getString("title");


                        double targetLatitude = Double.parseDouble(latitude); // Replace with the target latitude
                        double targetLongitude = Double.parseDouble(longitude); // Replace with the target longitude

// Create a LatLng object for the target location
                        LatLng targetLatLng = new LatLng(targetLatitude, targetLongitude);

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(targetLatLng));
                        mMap.addMarker(new MarkerOptions().position(targetLatLng).title("Target Location"));

// Optionally, you can animate the camera movement with a specified zoom level
                        float zoomLevel = 15.0f; // Adjust as needed
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, zoomLevel));
                        // Use the retrieved data as needed
                        // For example, you can log or display the data
                        // Log.d("Firestore", "Latitude: " + latitude + ", Longitude: " + longitude + ", Title: " + title);
                    }
                }
            } else {

                Toast.makeText(MapActivity.this,"No Location Exists Add new ",Toast.LENGTH_LONG).show();
                mAddLocation.setVisibility(View.VISIBLE);
                getLocationPermission();
                // Handle errors here
                // Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });
    }

    private void init(){
        Log.d(TAG,"init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN ||
                        event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate();

                }
                return false;
            }
        });

        mAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });


        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();

            }
        });
        hideSoftKeyboard();
    }

    /**
     *
     */
    private void geoLocate(){
        Log.d(TAG,"geoLocate: geoLocating");
        String searchstring = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchstring,1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOExceeption: "+ e.getMessage());
        }

        if (list.size() > 0){
            address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }


    /**
     * Gets the user's device's current geoLocation
     */
    private void getDeviceLocation(){
        Log.d(TAG,"getDevicesLocation: getting devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if (mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            if (task.isSuccessful() && task.getResult() != null) {

                                Geocoder geocoder = new Geocoder(MapActivity.this);
                                try {
                                    myAddress =geocoder.getFromLocation(currentLocation
                                                    .getLatitude()
                                            ,currentLocation.getLongitude()
                                            ,1);

                                    if (myAddress.size() > 0) {
                                        address = myAddress.get(0);
                                        mSearchText.setText(address.getAddressLine(0));
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                moveCamera(new LatLng(currentLocation.getLatitude(),
                                        currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                            }

                        }
                        else{
                            Log.d(TAG,"onComplete: location not found");
                            Toast.makeText(MapActivity.this,
                                    "unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecurityException" + e.getMessage());
        }
    }


    /**
     * Moves around the map
     *
     * @param latLng
     * @param zoom
     * @param title
     */
    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG,"moveCamera: moving the camera to: lat:" +latLng.latitude +", lng: " +latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }


    /**
     * Initializes the map
     */
    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    /**
     * Prompts user if app is allowed to get Location info
     */
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                .ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * checks if user allowed app to use location or not
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionResult: called");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: permission granted");
                    mLocationPermissionGranted = true;
                    //Initialize map
                    initMap();
                }
            }
        }
    }

    /**
     * What to do when user opens Map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission
                            .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * On back pressed, checks type of user and goes to appropriate screen.
     */
    @Override
    public void onBackPressed() {

        finish();
//        User user = UserController.getController().getUser();
//        if (user.getClass() == Patient.class){
//
//
//
//        }
//        if(user.getClass() == Caregiver.class){
//
//        }
//        this.finish();

    }

    /**
     * Displays user prompts
     */
    public void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("MDBook");
        alert.setMessage("Are you sure you want to use this location?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (address != null) {

                    SaveAddress(address);

                   // finish();

                }
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }

    private void SaveAddress(Address address) {

      String Lat = String.valueOf( address.getLatitude());
        String  Long = String.valueOf( address.getLongitude());
        String   Title = String.valueOf(  address.getAddressLine(0));



        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a new map to store the data
        Map<String, Object> data = new HashMap<>();
        data.put("latitude", Lat);
        data.put("longitude", Long);
        data.put("title", Title);
        data.put("problemId",id);

// Replace "locations" with the name of your Firestore collection
// Add a new document with a unique ID
        db.collection("locations")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    // You can log or handle success here

                    finish();
                    // Log.d("Firestore", "DocumentSnapshot added with ID: " + documentId);
                })
                .addOnFailureListener(e -> {
                    // Handle failures here
                    // Log.e("Firestore", "Error adding document", e);
                });

    }

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapFragment != null) {
            this.getSupportFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }
    */
}
