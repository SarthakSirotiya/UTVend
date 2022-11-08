package com.trescaballeros.utvend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setTitle("LIST_LIST") // TODO strings.xml spanish

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addButton -> {
                Toast.makeText(this, "add item selected", Toast.LENGTH_SHORT).show()
            }
            R.id.mapButton -> {
                Toast.makeText(this, "list item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GoogleMapActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.settingsButton -> {
                Toast.makeText(this, "settings item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}