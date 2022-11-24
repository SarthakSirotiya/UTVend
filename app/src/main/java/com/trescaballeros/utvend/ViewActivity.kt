package com.trescaballeros.utvend

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import coil.load
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.trescaballeros.utvend.databinding.ActivityViewBinding
import com.trescaballeros.utvend.model.VendingMachine

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding
    val PICK_IMAGE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Details"
        val gson = Gson()
        val vm: VendingMachine = gson.fromJson(intent.extras?.getString("vm"),
            VendingMachine::class.java) as VendingMachine
//        val defaultImage = "https://media.istockphoto.com/id/1026540906/photo/students-" +
//                "couple.jpg?s=612x612&w=0&k=20&c=8ZROdXvd2eMEGYmYSf_j9M_KB3TG3utGj9-D_UME6cs="
//        binding.vmImageView.load(defaultImage)
//        binding.geoNotesTextView.text = vm.geo_notes
//        binding.extraNotesTextView.text = vm.extra_notes
//        val timestampString = "Last edited at ${vm.timestamp}"
//        binding.timestampTextView.text = timestampString
//        binding.directionsButton.setOnClickListener {
//            val uri = Uri.parse("google.navigation:q=${vm.location.latitude}," +
//                    "${vm.location.longitude}&mode=w")
//            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)
//        }

//        binding.viewImageView.load(defaultImage)
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child(vm.image)
        Log.e("HUMBERTO", vm.image)
        imageRef.downloadUrl.addOnSuccessListener {Uri->

            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.viewImageView)

        }
//        binding.viewImageView.load(imageref)
//
//        Glide.with(this).load(imageRef).into(binding.viewImageView)

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


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.viewImageView.load(selectedImage)
                binding.viewSubmitButton.isEnabled = true
            }
        } else if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                val selectedImage = data?.getData()
                binding.viewImageView.load(selectedImage)
                binding.viewSubmitButton.isEnabled = true
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