package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditDetails : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        val updateName: EditText = findViewById(R.id.updateName)
        val updateEmail: EditText = findViewById(R.id.updateEmail)
        val updatePhone: EditText = findViewById(R.id.updatePhone)
        val updatePassword: EditText = findViewById(R.id.updatePassword)

        val EditDetails: Button = findViewById(R.id.EditDetails)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // fetch user data from Firestore
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("name")
                    val email = document.getString("email")
                    val phone = document.getString("phone")
                    val password = document.getString("password")

                    updateName.setText(name)
                    updateEmail.setText(email)
                    updatePhone.setText(phone)
                    updatePassword.setText(password)

                } else {
                    Log.d("EditProfile", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("EditProfile", "get failed with ", exception)
                // handle exception here
            }

        EditDetails.setOnClickListener {
            val newName = updateName.text.toString().trim()
            val newEmail = updateEmail.text.toString().trim()
            val newPhone = updatePhone.text.toString().trim()
            val newPassword = updatePassword.text.toString().trim()

            if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(newEmail)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPhoneNumber(newPhone)) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // update userName data in Firestore
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .update("name", newName)
                .addOnSuccessListener {

                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this, profile::class.java)
                    )

                }
                .addOnFailureListener { exception ->
                    Log.d("EditProfile", "update failed with ", exception)
                    // handle exception here
                }

//            //update userEmail
//            db.collection("users")
//                .document(auth.currentUser!!.uid)
//                .update("email", newEmail)
//                .addOnSuccessListener {
//                    // Update the user email in Firebase Authentication
//                    auth.currentUser!!.updateEmail(newEmail)
//                        .addOnSuccessListener {
//                            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
//                            startActivity(
//                                Intent(this, profile::class.java)
//                            )
//                        }
//                        .addOnFailureListener { exception ->
//                            Log.d("EditProfile", "update failed with ", exception)
//                            // handle exception here
//                        }
//
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("EditProfile", "update failed with ", exception)
//                    // handle exception here
//                }

            // update userEmail
            val user = auth.currentUser
            val credential = EmailAuthProvider.getCredential(user?.email!!, newPassword)

            user?.reauthenticate(credential)?.addOnSuccessListener {
                user.updateEmail(newEmail).addOnSuccessListener {
                    // Update the user's email in Firestore
                    db.collection("users")
                        .document(auth.currentUser!!.uid)
                        .update("email", newEmail)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, profile::class.java))
                        }
                        .addOnFailureListener { exception ->
                            Log.d("EditProfile", "update failed with ", exception)
                            // handle exception here
                        }
                }.addOnFailureListener { exception ->
                    Log.d("EditProfile", "update failed with ", exception)
                    // handle exception here
                }
            }?.addOnFailureListener { exception ->
                Log.d("EditProfile", "reauthentication failed with ", exception)
                // handle exception here
            }

            //update user Phone
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .update("phone", newPhone)
                .addOnSuccessListener {

                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this, profile::class.java)
                    )

                }
                .addOnFailureListener { exception ->
                    Log.d("EditProfile", "update failed with ", exception)
                    // handle exception here
                }

            //update Password
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .update("password", newPassword)
                .addOnSuccessListener {

                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this, profile::class.java)
                    )

                }
                .addOnFailureListener { exception ->
                    Log.d("EditProfile", "update failed with ", exception)
                    // handle exception here
                }



        }



    }
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(number: String): Boolean {
        return number.length == 10 && TextUtils.isDigitsOnly(number)
    }
}