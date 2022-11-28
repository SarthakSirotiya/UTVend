package com.trescaballeros.utvend

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import coil.load
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.trescaballeros.utvend.databinding.ActivityViewBinding
import com.trescaballeros.utvend.model.GeopointSerializer
import com.trescaballeros.utvend.model.TimestampSerializer
import com.trescaballeros.utvend.model.VendingMachine
import java.io.ByteArrayOutputStream
import java.util.*

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding
    val PICK_IMAGE = 1


    //******************************************************
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
                        task->val location: Location?=task.result
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
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
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
    //******************************************************


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Details"
        val gson = GsonBuilder()
            .registerTypeAdapter(Timestamp::class.java, TimestampSerializer())
            .registerTypeAdapter(GeoPoint::class.java, GeopointSerializer())
            .create()
        val vm: VendingMachine = gson.fromJson(intent.extras?.getString("vm"),
            VendingMachine::class.java) as VendingMachine

        if(vm.id == ""){
            //need to write in vm.id
            vm.id = (vm.image.substring(0,vm.image.length-5))
        }



        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child(vm.image)
        imageRef.downloadUrl.addOnSuccessListener {Uri->

            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.viewImageView)

        }

        binding.viewGeoNotesEditText.setText(vm.geo_notes)
        binding.viewGeoNotesEditText.isEnabled = true
        binding.viewExtraNotesEditText.setText(vm.extra_notes)
        binding.viewExtraNotesEditText.isEnabled = true


        binding.viewSelectButton.setOnClickListener {
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

//            binding.viewSubmitButton.isEnabled = true
        }

        binding.viewGeoNotesEditText.addTextChangedListener {
            binding.viewSubmitButton.isEnabled = true
        }

        binding.viewExtraNotesEditText.addTextChangedListener {
            binding.viewSubmitButton.isEnabled = true
        }

        binding.viewSubmitButton.setOnClickListener {
//            val uniqueID = UUID.randomUUID().toString()
            val uniqueID = vm.id
            var badGeo = GeoPoint(0.0,0.0)
            val geoNotes = binding.viewGeoNotesEditText.getText().toString()
            val extraNotes = binding.viewExtraNotesEditText.getText().toString()
            val curTime = Timestamp.now()

            //Design decision needed, reuse or update geo location of point? - HUMBERTO
            badGeo = vm.location

            //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            //getCurrentLocation()

            val newVM = hashMapOf(
                "extra_notes" to extraNotes,
                "geo_notes" to geoNotes,
                "image" to uniqueID + ".jpeg",
                "location" to badGeo,
                "timestamp" to Timestamp(Date())
            )

            //FirebaseFirestore.getInstance().collection("vms_1").document(uniqueID).set(newVM)

            FirebaseFirestore.getInstance().collection("vms_1").document(vm.id).update("geo_notes", geoNotes,"extra_notes", extraNotes)





            val storage = Firebase.storage
            // Create a storage reference from our app
            val storageRef = storage.reference

            // Create reference for vending machine image
            val vendRef = storageRef.child(uniqueID+".jpeg")

            // Get the data from an ImageView as bytes
//            binding.submitImageView.isDrawingCacheEnabled = true
//            binding.submitImageView.buildDrawingCache()
            val bitmap = (binding.viewImageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var uploadTask = vendRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.e("HUMBERTO", "FAIL")
                Toast.makeText(this, "Upload failed.", Toast.LENGTH_SHORT).show()
                val mediaPlayer = MediaPlayer.create(this, R.raw.critical)
                mediaPlayer.setOnCompletionListener { val mine = 5 }
                mediaPlayer.start()
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Log.e("HUMBERTO", "SUCCESS")
                Toast.makeText(this, "Upload successful!", Toast.LENGTH_SHORT).show()
                val mediaPlayer = MediaPlayer.create(this, R.raw.whoosh)
                mediaPlayer.setOnCompletionListener { val mine = 5 }
                mediaPlayer.start()
                finish()
            }
//            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.viewImageView.load(selectedImage)
                binding.viewSubmitButton.isEnabled = true
                binding.viewSelectButton.setText("SELECT AN IMAGE")
            }
        } else if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.viewImageView.load(selectedImage)
                binding.viewSubmitButton.isEnabled = true
                binding.viewSelectButton.setText("SELECT AN IMAGE")
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}