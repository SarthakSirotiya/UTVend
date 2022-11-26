package com.trescaballeros.utvend

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
//import androidx.core.location.LocationManagerCompat.isLocationEnabled
//import androidx.core.location.LocationManagerCompat.getCurrentLocation
import coil.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.trescaballeros.utvend.databinding.ActivitySubmitBinding
import java.io.ByteArrayOutputStream
import java.util.*


//private const val TAG = "MainActivity"
//private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class SubmitActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySubmitBinding
    val PICK_IMAGE = 1
    private lateinit var imageData: Uri
//    private lateinit var locationManager: LocationManager
//    private lateinit var tvGpsLocation: TextView
//    private val locationPermissionCode = 2

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var myLat: Double = 0.0
    private var myLng: Double = 0.0



    private fun getCurrentLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                //final lat and lng code here
                Log.e("hello" , "world")

                if(ActivityCompat.checkSelfPermission(
                  this,
                  android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                    requestPermission()
                    return
                }

                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) {
                    task->val location:Location?=task.result
                    if(location == null){
                        Toast.makeText(applicationContext,"Null Recieved", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(applicationContext, "Get Success", Toast.LENGTH_SHORT).show()
                        myLat = location.latitude
                        myLng = location.longitude
                    }
                }
            } else {
                //setting open here
                Toast.makeText(applicationContext, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent=Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else {
            //request permission here
            requestPermission()
        }


    }

    private fun isLocationEnabled(): Boolean {
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //DISPLAY TO USER THAT PERMISSION WAS GRANTED TOAST?
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)
        binding = ActivitySubmitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Submit")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        binding.submitSelectButton.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.setType("image/*")
            //getIntent.setAction(Intent.ACTION_GET_CONTENT)
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            //pickIntent.setAction(Intent.ACTION_PICK)
            pickIntent.setType("image/*")
            val chooserIntent = Intent.createChooser(getIntent, "Select With:")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, Array<Intent>(1) { pickIntent })
            val photoUri = 0
            chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(chooserIntent, PICK_IMAGE)


        }

        binding.submitSubmitButton.setOnClickListener {
            val uniqueID = UUID.randomUUID().toString()
            var badGeo = GeoPoint(0.0,0.0)
            val geoNotes = binding.submitGeoNotesEditText.getText().toString()
            val extraNotes = binding.submitExtraNotesEditText.getText().toString()
            val curTime = Timestamp.now()
            //val newVM = VendingMachine(uniqueID, badGeo, uniqueID, binding.geoNotesEditText.getText().toString(), binding.extraNotesEditText.getText().toString(), Timestamp.now(), )

            getCurrentLocation()
            badGeo = GeoPoint(myLat, myLng)

            //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            //getCurrentLocation()

            val newVM = hashMapOf(
                "extra_notes" to extraNotes,
                "geo_notes" to geoNotes,
                "image" to uniqueID + ".jpeg",
                "location" to badGeo,
                "timestamp" to Timestamp(Date())
            )

            FirebaseFirestore.getInstance().collection("vms_1").document(uniqueID).set(newVM)
            val storage = Firebase.storage
            // Create a storage reference from our app
            val storageRef = storage.reference

            // Create a reference to "mountains.jpg"
            val mountainsRef = storageRef.child(uniqueID+".jpeg")

            // Create a reference to 'images/mountains.jpg'
            val mountainImagesRef = storageRef.child("images/"+uniqueID+".jpeg")

            // Get the data from an ImageView as bytes
//            binding.submitImageView.isDrawingCacheEnabled = true
//            binding.submitImageView.buildDrawingCache()
            val bitmap = (binding.submitImageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {

                val selectedImage = data?.getData()
                binding.submitImageView.load(selectedImage)
                binding.submitSubmitButton.isEnabled = true
                binding.submitGeoNotesEditText.isEnabled = true
                binding.submitGeoNotesEditText.setHint("Put geo notes here")
                binding.submitExtraNotesEditText.isEnabled = true
                binding.submitExtraNotesEditText.setHint("Put extra notes here")
            }
        } else if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.submitImageView.load(selectedImage)
                binding.submitSubmitButton.isEnabled = true
                binding.submitGeoNotesEditText.isEnabled = true
                binding.submitGeoNotesEditText.setHint("Put geo notes here")
                binding.submitExtraNotesEditText.isEnabled = true
                binding.submitExtraNotesEditText.setHint("Put extra notes here")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}