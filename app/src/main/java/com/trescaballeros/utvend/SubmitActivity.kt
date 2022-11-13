package com.trescaballeros.utvend

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.load
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
            val uniqueID = UUID.randomUUID().toString()
            val badGeo = GeoPoint(0.0,0.0)
            val geoNotes = binding.geoNotesEditText.getText().toString()
            val extraNotes = binding.extraNotesEditText.getText().toString()
            val curTime = Timestamp.now()
            //val newVM = VendingMachine(uniqueID, badGeo, uniqueID, binding.geoNotesEditText.getText().toString(), binding.extraNotesEditText.getText().toString(), Timestamp.now(), )

            val newVM = hashMapOf(
                "extra_notes" to extraNotes,
                "geo_notes" to geoNotes,
                "image" to uniqueID + ".jpg",
                "location" to GeoPoint(0.0, 0.0),
                "timestamp" to Timestamp(Date())
            )

            FirebaseFirestore.getInstance().collection("vms_1").document(uniqueID).set(newVM)

            val storage = Firebase.storage
            // Create a storage reference from our app
            val storageRef = storage.reference
//
//            // Create a reference with an initial file path and name
//            val pathReference = storageRef.child("images/Sunrise_On_Rails.jpg")
//
//            // Create a reference to a file from a Google Cloud Storage URI
//            val gsReference = storage.getReferenceFromUrl("gs://bucket/images/Sunrise_On_Rails.jpg")
//
////            // Create a reference from an HTTPS URL
////            // Note that in the URL, characters are URL escaped!
////            val httpsReference = storage.getReferenceFromUrl(
////                "https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg")
//            val ONE_MEGABYTE: Long = 1024 * 1024
//            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
//                // Data for "images/island.jpg" is returned, use this as needed
//                binding.submitImageView.load(ONE_MEGABYTE)
//            }.addOnFailureListener {
//                // Handle any errors
//            }



            // Create a reference to "mountains.jpg"
            val mountainsRef = storageRef.child(uniqueID+".jpg")

            // Create a reference to 'images/mountains.jpg'
            val mountainImagesRef = storageRef.child("images/"+uniqueID+".jpg")

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}