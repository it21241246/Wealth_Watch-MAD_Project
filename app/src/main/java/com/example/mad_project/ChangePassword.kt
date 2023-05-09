package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ChangePassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = auth.currentUser!!

        val prePass: EditText = findViewById(R.id.prePass)
        val newPass: EditText = findViewById(R.id.newPass)
        val conPass: EditText = findViewById(R.id.conPass)

        val chngPwd: Button = findViewById(R.id.chngPwd)

        chngPwd.setOnClickListener {
            val pPass = prePass.text.toString().trim()
            val nPass = newPass.text.toString().trim()
            val cPass = conPass.text.toString().trim()

            if (pPass.isEmpty() || nPass.isEmpty() || cPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (nPass.length < 8) {
                Toast.makeText(
                    this,
                    "New password should be at least 8 characters long",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (nPass != cPass) {
                Toast.makeText(
                    this,
                    "New password and Confirm password fields do not match",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, pPass)

                currentUser.reauthenticate(credential)
                    .addOnCompleteListener() { authTask ->
                        if (authTask.isSuccessful) {
                            currentUser.updatePassword(nPass)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        //update password in firestone

                                        db.collection("users").document(currentUser.uid)
                                            .update("password", nPass)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    this,
                                                    "Password updated successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                auth.signOut()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    this,
                                                    "Failed to update password in Firestore",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }else{
                                        if (authTask.exception is FirebaseAuthInvalidCredentialsException){
                                            Toast.makeText(this, "Invalid previous password", Toast.LENGTH_SHORT).show()
                                        }else{
                                            Toast.makeText(this, "Failed to authenticate user", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        }
                    }
            }

        }
//        val nav: NavigationBarView = findViewById(R.id.navbar)
//
//        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
//
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//                when (item.itemId) {
//
//                    R.id.home -> {
//                        val intent = Intent(this@ChangePassword, MainActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                    R.id.goals -> {
//                        val intent = Intent(this@ChangePassword, HomePageActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                    R.id.stats -> {
//                        val intent = Intent(this@ChangePassword, ViewPage::class.java)
//                        startActivity(intent)
//                    }
//
//                    R.id.settings -> {
//                        val intent = Intent(this@ChangePassword, profile::class.java)
//                        startActivity(intent)
//                    }
//
//                }
//
//                return true
//            }
//        })

    }

}


