package com.example.laundryhub.Page

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundryhub.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapPage : AppCompatActivity(), OnMapReadyCallback {
    private var myMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_page)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        val buttonBack: ImageButton = findViewById(R.id.btn_back)

        mapFragment?.getMapAsync(this)

        buttonBack.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        val location = LatLng(-6.205597, 106.818352)
        myMap?.addMarker(
            MarkerOptions().position(location).title("Laundry Hub")
        )
        myMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
    }

    override fun onDestroy() {
        myMap = null
        super.onDestroy()
    }
}