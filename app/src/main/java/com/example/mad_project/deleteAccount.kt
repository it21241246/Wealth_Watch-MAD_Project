package com.example.mad_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class deleteAccount : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val uid = firebaseAuth.currentUser?.uid

        // Delete the user's document from Firestore
        if (uid != null) {
            firestore.collection("users").document(uid)
                .delete()
                .addOnSuccessListener {
                    // Delete the user's authentication account
                    firebaseAuth.currentUser?.delete()
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show()
                            // Redirect to the Register page
                            startActivity(
                                android.content.Intent(this, SignUp::class.java)
                            )
                        }
                        ?.addOnFailureListener { exception ->
                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
        }

    }
}