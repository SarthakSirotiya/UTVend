package com.trescaballeros.utvend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import coil.ImageLoader
import coil.request.ImageRequest
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.trescaballeros.utvend.databinding.ActivityGoogleMapBinding
import com.trescaballeros.utvend.model.VendingMachine

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapBinding
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            onSignInResult(res)
        }

    class VMInfoWindow(mContext: Context) : InfoWindowAdapter {

        private val view: View = (mContext as Activity).layoutInflater.inflate(
            R.layout.vm_info_window, null
        )
        private val imageView: ImageView = view.findViewById(R.id.vmImageView)

        override fun getInfoContents(p0: Marker): View {
            while ((p0.tag as VendingMachine).drawable == null) {
                continue
            }
            imageView.setImageDrawable((p0.tag as VendingMachine).drawable)
            return view
        }

        override fun getInfoWindow(p0: Marker): View? {
            return null
        }
    }

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
                if (FirebaseAuth.getInstance().currentUser != null) {
                    Toast.makeText(this, "add item selected", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SubmitActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.listButton -> {
                Toast.makeText(this, "list item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.settingsButton -> {
                Toast.makeText(this, "settings item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.profileButton -> {
                Toast.makeText(this, "profile item selected", Toast.LENGTH_SHORT).show()
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    startSignInIntent()
                } else {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
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
        loadVendingMachines()
        mMap.setInfoWindowAdapter(VMInfoWindow(this))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(utAustin))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.setOnInfoWindowClickListener { marker ->
            val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            val vm = marker.tag as VendingMachine
            val viewIntent = Intent(this, ViewActivity::class.java)
            val json = gson.toJson(vm)
            viewIntent.putExtra("vm", json)
            startActivity(viewIntent)
        }
    }

    private fun loadVendingMachines() {
        val loader = ImageLoader(this)
        val storageRef = Firebase.storage.reference
        FirebaseFirestore.getInstance().collection("vms_1").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("vm_map", document.toString())
                    val vm = document.toObject<VendingMachine>()
                    vm.id = document.id
                    vm.drawable =
                        AppCompatResources.getDrawable(this, R.drawable.vending_machine_loading)
                    val imageRef = storageRef.child(vm.image)
                    val coords = LatLng(vm.location.latitude, vm.location.longitude)
                    val marker = mMap.addMarker(MarkerOptions().position(coords))
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val req = ImageRequest.Builder(this)
                            // get image url from vending machine object
                            .data(uri)
                            .allowHardware(false)
                            .target { drawable ->
                                vm.drawable = drawable
                            }
                            .build()
                        loader.enqueue(req)
                    }
                    marker?.tag = vm
                }
            }
    }

    private fun startSignInIntent() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            //.setLogo() TODO add logo to login page
            // should automatically use app default theme
            // can add theme manually if necessary
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this, "Logged in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("login", "error resultCode of ${result.resultCode}")
        }
    }
}