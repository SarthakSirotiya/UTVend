package com.trescaballeros.utvend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.trescaballeros.utvend.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lightDarkButton.setOnClickListener {
            Toast.makeText(this, "yeah this doesn't do anything yet", Toast.LENGTH_SHORT).show()
        }

        binding.englishSpanishButton.setOnClickListener {
            Toast.makeText(this, "this doesn't do anything yet either", Toast.LENGTH_SHORT).show()
        }

        // Lab3
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("SETTINGS") // TODO strings.xml spanish


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}