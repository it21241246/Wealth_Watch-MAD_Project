package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.mad_project.databinding.ActivityProfileBinding
import com.example.mad_project.databinding.ActivityViewDetailsBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewDetails : AppCompatActivity() {

    private lateinit var binding: ActivityViewDetailsBinding

    private lateinit var showNameHeading: TextView
    private lateinit var showEmailHeading: TextView

    private lateinit var showName: TextView
    private lateinit var showEmail: TextView
    private lateinit var showPhone: TextView
    //private lateinit var showPassword: TextView


    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNameHeading = findViewById(R.id.UserNameHeading)
        showEmailHeading = findViewById(R.id.UserEmailHeading)

        showName = findViewById(R.id.showName)
        showEmail = findViewById(R.id.showEmail)
        showPhone = findViewById(R.id.showPhone)
        //showPassword = findViewById(R.id.showPassword)

        binding.EditButton.setOnClickListener {
            startActivity(
                Intent(this, EditDetails::class.java)
            )
        }


        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        val docRef = db.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val nameHead = document.data!!["name"].toString()
                    val emailHead = document.data!!["email"].toString()

                    val name = document.data!!["name"].toString()
                    val email = document.data!!["email"].toString()
                    val phone = document.data!!["phone"].toString()
                    val password = document.data!!["password"].toString()

                    showNameHeading.text = nameHead
                    showEmailHeading.text = emailHead

                    showName.text = name
                    showEmail.text = email
                    showPhone.text = phone
                    //showPassword.text = password
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
                        val intent = Intent(this@ViewDetails, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@ViewDetails, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@ViewDetails, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@ViewDetails, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })
    }
}