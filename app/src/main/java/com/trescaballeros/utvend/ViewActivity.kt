package com.trescaballeros.utvend

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import coil.load
import com.google.gson.Gson
import com.trescaballeros.utvend.databinding.ActivityViewBinding
import com.trescaballeros.utvend.model.VendingMachine

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Details"
        val gson = Gson()
        val vm: VendingMachine = gson.fromJson(intent.extras?.getString("vm"),
            VendingMachine::class.java) as VendingMachine
        val defaultImage = "https://media.istockphoto.com/id/1026540906/photo/students-" +
                "couple.jpg?s=612x612&w=0&k=20&c=8ZROdXvd2eMEGYmYSf_j9M_KB3TG3utGj9-D_UME6cs="
        binding.vmImageView.load(defaultImage)
        binding.geoNotesTextView.text = vm.geo_notes
        binding.extraNotesTextView.text = vm.extra_notes
        val timestampString = "Last edited at ${vm.timestamp}"
        binding.timestampTextView.text = timestampString
        binding.directionsButton.setOnClickListener {
            val uri = Uri.parse("google.navigation:q=${vm.location.latitude}," +
                    "${vm.location.longitude}&mode=w")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}