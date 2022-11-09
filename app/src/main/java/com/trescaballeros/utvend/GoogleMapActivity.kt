package com.trescaballeros.utvend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.toObject
import com.trescaballeros.utvend.databinding.ActivityGoogleMapBinding
import com.trescaballeros.utvend.model.VendingMachine

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lab3
        setTitle("MAP_MAP") // TODO strings.xml spanish


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addButton -> {
                Toast.makeText(this, "add item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SubmitActivity::class.java)
                startActivity(intent)
            }
            R.id.listButton -> {
                Toast.makeText(this, "list item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.settingsButton -> {
                Toast.makeText(this, "settings item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val utAustin = LatLng(30.2849, -97.7341)
        loadVendingMachines(mMap)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(utAustin))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
    }

    private fun loadVendingMachines(map: GoogleMap){
        FirebaseFirestore.getInstance().collection("vms_1").get()
            .addOnSuccessListener {result ->
                for (document in result) {
                    val vm = document.toObject<VendingMachine>()
                    val coords = LatLng(vm.location.latitude, vm.location.longitude)
                    map.addMarker(MarkerOptions().position(coords))
                }
            }
    }
}