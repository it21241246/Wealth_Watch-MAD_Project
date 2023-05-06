package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.mad_project.databinding.ActivityProfileBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var showName: TextView
    private lateinit var showEmail: TextView

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showName = findViewById(R.id.UserNameHeading)
        showEmail = findViewById(R.id.UserEmailHeading)


        binding.ViewButton.setOnClickListener {
            startActivity(
                Intent(this, ViewDetails::class.java)
            )
        }

        binding.EditButton.setOnClickListener {
            startActivity(
                Intent(this, EditDetails::class.java)
            )
        }

        binding.LogOutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(
                Intent(this, Login::class.java)
            )
        }

        binding.privacyButton.setOnClickListener {
            startActivity(
                Intent(this, Privacy::class.java)
            )
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        val docRef = db.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.data!!["name"].toString()
                    val email = document.data!!["email"].toString()

                    showName.text = name
                    showEmail.text = email
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@profile, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@profile, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@profile, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@profile, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })


    }
}
