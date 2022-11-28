package com.trescaballeros.utvend

import android.app.AlertDialog
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import com.trescaballeros.utvend.databinding.ActivitySettingsBinding
import java.util.*


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    var systemLanguage: String = Locale.getDefault().language

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeButton.setOnClickListener {
            selectTheme()
        }

        // Lab3
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.settings)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun selectTheme() {
        val ab = AlertDialog.Builder(this)
        ab.setTitle(getString(R.string.choose_theme))
        ab.setItems(arrayOf(getString(R.string.light), getString(R.string.dark),
            getString(R.string.system_default))) { _, i ->
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