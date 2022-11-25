package com.trescaballeros.utvend

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import com.trescaballeros.utvend.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeButton.setOnClickListener {
            selectTheme()
        }

        binding.languageButton.setOnClickListener {
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

    private fun selectTheme() {
        val ab = AlertDialog.Builder(this)
        ab.setTitle("Choose theme")
        ab.setSingleChoiceItems(
            arrayOf("Light", "Dark", "System Default"),
            -1
        ) { _, i ->
            setDefaultNightMode(
                when (i) {
                    0 -> MODE_NIGHT_NO
                    1 -> MODE_NIGHT_YES
                    else -> MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }
        ab.show()
    }

}