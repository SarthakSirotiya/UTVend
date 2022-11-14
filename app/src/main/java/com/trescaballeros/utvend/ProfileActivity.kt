package com.trescaballeros.utvend

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.trescaballeros.utvend.databinding.ActivityProfileBinding
import com.trescaballeros.utvend.MapActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Profile" // TODO strings.xml spanish

        // assuming user non-null b/c user was non-null in MainActivity
        user = Firebase.auth.currentUser!!

        // populate user information
        val defaultPhoto: String = "https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383" +
                "_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg"
        if (user.photoUrl == null) {
            binding.profileImageView.load(defaultPhoto)
        } else {
            binding.profileImageView.load(user.photoUrl)
        }
        binding.nameTextView.text = user.displayName
        binding.emailTextView.text = user.email

        binding.signoutButton.setOnClickListener {
            signOut()
        }

        binding.deleteButton.setOnClickListener {
            deleteAccountPrompt()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        AuthUI
            .getInstance()
            .signOut(this)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Signed out successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Something went wrong while signing out.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        finish()
    }

    private fun deleteAccountPrompt() {
        val adBuilder = AlertDialog.Builder(this)
        adBuilder.setTitle("Confirm deletion")
        adBuilder.setMessage("Do you really want to delete your account?")
        adBuilder.setPositiveButton("Yes") { _, _ -> deleteAccount() }
        adBuilder.setNegativeButton("No") { _, _ -> }
        adBuilder.show()
    }

    private fun deleteAccount() {
        user
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        finish()
    }
}