package com.trescaballeros.utvend

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.trescaballeros.utvend.databinding.ActivitySubmitBinding

private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class SubmitActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySubmitBinding

    val PICK_IMAGE = 1

//    private lateinit var locationManager: LocationManager
//    private lateinit var tvGpsLocation: TextView
//    private val locationPermissionCode = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)
        binding = ActivitySubmitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Submit")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.selectButton.setOnClickListener {
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

        binding.submitButton.setOnClickListener {


            //old if -ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            //                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

//            if(isLocationPermissionGranted()) {
//                if(isLocationPermissionGranted()) {
//                    setTitle("fjdlajlkfdsa")
//                    getLocation()
//                }else{
//                    setTitle("else")
//                }
//            } else {
//                setTitle("BIGelse")
//            }
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {
//                if(isLocationPermissionGranted()){
//                    var currentLocation: Location? = null
//                    lateinit var locationManager: LocationManager
//                    locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//                    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
////------------------------------------------------------//
//                    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//                    val gpsLocationListener: LocationListener = object : LocationListener {
//                        override fun onLocationChanged(location: Location) {
////                    locationByGps= location
//                            currentLocation = location
//                        }
//
//                        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//                        override fun onProviderEnabled(provider: String) {}
//                        override fun onProviderDisabled(provider: String) {}
//                    }
//                    var locationByGps = -1
//                    var locationByNetwork = -1
////------------------------------------------------------//
//                    val networkLocationListener: LocationListener = object : LocationListener {
//                        override fun onLocationChanged(location: Location) {
//                            locationByGps = location
////                    currentLocation = location
//                        }
//
//                        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//                        override fun onProviderEnabled(provider: String) {}
//                        override fun onProviderDisabled(provider: String) {}
//                    }
//
//                    if (hasGps) {
//                        locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER,
//                            5000,
//                            0F,
//                            gpsLocationListener
//                        )
//                    }
////------------------------------------------------------//
//                    if (hasNetwork) {
//                        locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            5000,
//                            0F,
//                            networkLocationListener
//                        )
//                    }
//
//                    val lastKnownLocationByGps =
//                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    lastKnownLocationByGps?.let {
//                        locationByGps = lastKnownLocationByGps
//                    }
////------------------------------------------------------//
//                    val lastKnownLocationByNetwork =
//                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                    lastKnownLocationByNetwork?.let {
//                        locationByNetwork = lastKnownLocationByNetwork
//                    }
////------------------------------------------------------//
//                    val latitude = -1
//                    val longitude = -1
//                    if (locationByGps != null && locationByNetwork != null) {
//                        if (locationByGps.accuracy > locationByNetwork!!.accuracy) {
//                            currentLocation = locationByGps
//                            latitude = currentLocation.latitude
//                            longitude = currentLocation.longitude
//                            // use latitude and longitude as per your need
//                        } else {
//                            currentLocation = locationByNetwork
//                            latitude = currentLocation.latitude
//                            longitude = currentLocation.longitude
//                            // use latitude and longitude as per your need
//                        }
//                    }
//                }
////
////
////
//            }
//            }
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, ) {
//        super.onActivityResult(requestCode, resultCode, data)
//    }


    //*&^%$#^#^%$#%^$#^

//    private fun getLocation() {
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
//        }
//        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//            var locationByGps = (Location)0
//            val gpsLocationListener: LocationListener = object : LocationListener {
//                override fun onLocationChanged(location: Location) {
//                    locationByGps = location
//                }
//
//                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//                override fun onProviderEnabled(provider: String) {}
//                override fun onProviderDisabled(provider: String) {}
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, gpsLocationListener)
//        }
//        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f)
//
////        locationManager.
//    }
//    fun onLocationChanged(location: Location) {
//        //tvGpsLocation = findViewById(R.id.textView)
//        //tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
//    }
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == locationPermissionCode) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    //*&^%$#^#^%$#%^$#^

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.submitImageView.load(selectedImage)
                binding.submitButton.isEnabled = true
                binding.geoNotesEditText.isEnabled = true
                binding.geoNotesEditText.setHint("Put geo notes here")
                binding.extraNotesEditText.isEnabled = true
                binding.extraNotesEditText.setHint("Put extra notes here")
            }
        } else if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.submitImageView.load(selectedImage)
                binding.submitButton.isEnabled = true
                binding.geoNotesEditText.isEnabled = true
                binding.geoNotesEditText.setHint("Put geo notes here")
                binding.extraNotesEditText.isEnabled = true
                binding.extraNotesEditText.setHint("Put extra notes here")
            }
        }
    }

//    private fun isLocationPermissionGranted(): Boolean {
//        return if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                212
//                //unsure what this value has to be, it is the "requestCode" and must be >=0
//            )
//            false
//        } else {
//            true
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}