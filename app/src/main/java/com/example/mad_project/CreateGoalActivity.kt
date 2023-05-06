package com.example.mad_project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import com.example.mad_project.R

class CreateGoalActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        val nameEditText = findViewById<EditText>(R.id.name_edit_text)
        val amountEditText = findViewById<EditText>(R.id.amount_edit_text)
        val descriptionEditText = findViewById<EditText>(R.id.description_edit_text)
        val categoryEditText = findViewById<EditText>(R.id.category_edit_text)

        val createButton = findViewById<Button>(R.id.create_button)
        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val description = descriptionEditText.text.toString()
            val category = categoryEditText.text.toString()

            // Create a new goal with the given data
            val goal = hashMapOf(
                "name" to name,
                "amount" to amount,
                "description" to description,
                "category" to category,
                "created_at" to Calendar.getInstance().timeInMillis
            )

            // Add the goal to the "goals" collection in Firestore
            db.collection("goals")
                .add(goal)
                .addOnSuccessListener {
                    // Display a success message or go back to the previous screen
                    finish()
                }
                .addOnFailureListener {
                    // Display an error message
                    TODO()
                }
        }
    }
}
