package com.trescaballeros.utvend

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.trescaballeros.utvend.databinding.ActivitySubmitBinding

class SubmitActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)
        binding = ActivitySubmitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Submit")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}