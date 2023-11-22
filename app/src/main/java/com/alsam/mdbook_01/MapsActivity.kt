package com.alsam.mdbook_01

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null
    private lateinit var searchEditText: EditText
    private lateinit var magnifyImageView: ImageView
    private lateinit var gpsCenterImageView: ImageView
    private lateinit var addLocationImageView: ImageView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        searchEditText = findViewById(R.id.input_search)
        magnifyImageView = findViewById(R.id.ic_magnify)
        gpsCenterImageView = findViewById(R.id.gpscenter)
        addLocationImageView = findViewById(R.id.addLocation)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        magnifyImageView.setOnClickListener {
            val searchQuery = searchEditText.text.toString().trim()
            performSearch(searchQuery)
        }

        gpsCenterImageView.setOnClickListener {
            centerOnUserLocation()
        }

        addLocationImageView.setOnClickListener {
            addLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        checkLocationPermission()
    }

    private fun performSearch(query: String) {
        // Implement search functionality
        // Use Geocoding or Places API to convert query to coordinates and move the map camera
        Toast.makeText(this, "Search: $query", Toast.LENGTH_SHORT).show()
    }

    private fun centerOnUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val userLatLng = LatLng(location.latitude, location.longitude)
                        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                    } else {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addLocation() {
        // Implement functionality to add a marker on the map
        mGoogleMap?.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions().position(latLng).title("Marker")
            mGoogleMap?.addMarker(markerOptions)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            enableMyLocation()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mGoogleMap?.isMyLocationEnabled = true
    }
}
