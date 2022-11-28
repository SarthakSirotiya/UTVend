package com.trescaballeros.utvend

import android.app.Activity
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.trescaballeros.utvend.adapter.VendingMachineAdapter
import com.trescaballeros.utvend.databinding.ActivityListBinding
import com.trescaballeros.utvend.model.JavaVendingMachine

class ListActivity : AppCompatActivity() {

    // XXX START
    // DO NOT DELETE -Manuel
    lateinit var myAdapter: VendingMachineAdapter
    // XXX END

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            onSignInResult(res)
        }
    private lateinit var binding: ActivityListBinding
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setTitle("LIST_LIST") // TODO strings.xml spanish
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // XXX START
        // DO NOT DELETE -Manuel
        // Set up query and options with it
        val query: Query
        = FirebaseFirestore.getInstance().collection("vms_1")
        val options: FirestoreRecyclerOptions<JavaVendingMachine>
        = FirestoreRecyclerOptions.Builder<JavaVendingMachine>().setQuery(query, JavaVendingMachine::class.java).build()


        myAdapter = VendingMachineAdapter(this, options)

        val recyclerView = binding.recyclerViewId
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true) // maybe remove this
        recyclerView.adapter = myAdapter
        setTitle("list num: " +  myAdapter.itemCount)
        // XXX END



        binding.floatingButton.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val intent = Intent(this, SubmitActivity::class.java)
                startActivity(intent)
            }
            else {
                mediaPlayer = MediaPlayer.create(this, R.raw.critical)
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                    Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
                }
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                )
                mediaPlayer.start()
            }
        }

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
        myAdapter.notifyDataSetChanged() // slightly innefficient
        // "more specific change events"
    }
    // XXX END

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapButton -> {
                val intent = Intent(this, GoogleMapActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.settingsButton -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.profileButton -> {
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