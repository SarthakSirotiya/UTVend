package com.trescaballeros.utvend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.trescaballeros.utvend.adapter.VendingMachineAdapter
import com.trescaballeros.utvend.model.JavaVendingMachine

class MapActivity : AppCompatActivity() {

    // XXX START
    // DO NOT DELETE -Manuel
    private lateinit var myAdapter: VendingMachineAdapter
    // XXX END

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            onSignInResult(res)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setTitle("LIST_LIST") // TODO strings.xml spanish

        // XXX START
        // DO NOT DELETE -Manuel
        // Set up query and options with it
        val query: Query
        = FirebaseFirestore.getInstance().collection("vms_1")
        val options: FirestoreRecyclerOptions<JavaVendingMachine>
        = FirestoreRecyclerOptions.Builder<JavaVendingMachine>().setQuery(query, JavaVendingMachine::class.java).build()

        // Create VendingMachineAdapter (FirestoreRecyclerAdapter)

        myAdapter = VendingMachineAdapter(this, options)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_id)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true) // maybe remove this
        recyclerView.adapter = myAdapter
        setTitle("list num: " + myAdapter.itemCount)
//
//        setTitle("getItemCt: " + myAdapter.getItemCount())
        // XXX END



    }

    // XXX START
    // DO NOT DELETE -Manuel
    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    // XXX
    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }
    // XXX END

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addButton -> {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    Toast.makeText(this, "add item selected", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SubmitActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.mapButton -> {
                Toast.makeText(this, "list item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GoogleMapActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.settingsButton -> {
                Toast.makeText(this, "settings item selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.profileButton -> {
                Toast.makeText(this, "profile item selected", Toast.LENGTH_SHORT).show()
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    startSignInIntent()
                }
                else {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startSignInIntent() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            //.setLogo() TODO add logo to login page
            // should automatically use app default theme
            // can add theme manually if necessary
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this, "Logged in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("login", "error resultCode of ${result.resultCode}")
        }
    }
}