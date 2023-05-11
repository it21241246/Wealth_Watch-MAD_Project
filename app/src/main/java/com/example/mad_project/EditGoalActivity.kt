package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditGoalActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        val nameEditText = findViewById<EditText>(R.id.edit_goal_name_edit_text)
        val amountEditText = findViewById<EditText>(R.id.edit_goal_amount_edit_text)
        val descriptionEditText = findViewById<EditText>(R.id.edit_goal_description_edit_text)
        val categoryEditText = findViewById<EditText>(R.id.edit_goal_category_edit_text)
        val savedAmountEditText = findViewById<EditText>(R.id.edit_goal_saved_amount_edit_text)


        val goalId = intent.getStringExtra("GOAL_ID")
        if (goalId == null) {
            finish()
            return
        }

        db.collection("goals")
            .document(goalId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val goal = documentSnapshot.toObject(Goal::class.java)
                if (goal != null) {
                    nameEditText.setText(goal.name)
                    amountEditText.setText(goal.amount.toString())
                    savedAmountEditText.setText(goal.savedAmount.toString())
                    descriptionEditText.setText(goal.description)
                    categoryEditText.setText(goal.category)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
        val updateButton = findViewById<Button>(R.id.update_goal_button)
        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDouble() ?: 0.0
            val savedAmount = savedAmountEditText.text.toString().toDouble() ?: 0.0
            val description = descriptionEditText.text.toString()
            val category = categoryEditText.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            if (name.isEmpty() || amount == null || savedAmount == null || description.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields to update!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            db.collection("goals")
                .document(goalId)
                .set(Goal(userId, goalId, name, amount, savedAmount, description, category))
                .addOnSuccessListener {
                    // Handle success
                    //toast message
                    Toast.makeText(this, "Goal Updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }


        val deleteButton = findViewById<Button>(R.id.delete_goal_button)
        deleteButton.setOnClickListener {
            db.collection("goals")
                .document(goalId)
                .delete()
                .addOnSuccessListener {
                    // Handle success
                    //toast message
                    Toast.makeText(this, "Goal Deleted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                }
        }

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@EditGoalActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@EditGoalActivity, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@EditGoalActivity, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@EditGoalActivity, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })
    }
}
