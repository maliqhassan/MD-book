package com.alsam.mdbook_01


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ViewMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var receiveLat: String? = null
    private var receiveLong: String? = null
    private var receiveTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Retrieve data from intent
        receiveLat = intent.getStringExtra("recieveLat")
        receiveLong = intent.getStringExtra("recieveLong")
        receiveTitle = intent.getStringExtra("recieveTitle")

        // Check if received data is not null
        if (receiveLat == null || receiveLong == null || receiveTitle == null) {
            Toast.makeText(this, "Invalid data received", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Convert latitude and longitude to Double
        val lat = receiveLat?.toDoubleOrNull()
        val long = receiveLong?.toDoubleOrNull()

        // Check if conversion was successful
        if (lat != null && long != null) {
            // Add a marker and move the camera
            val location = LatLng(lat, long)
            mMap.addMarker(MarkerOptions().position(location).title(receiveTitle))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        } else {
            Toast.makeText(this, "Invalid location data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
