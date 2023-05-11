package com.example.mad_project

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class EditGoalActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val db = Firebase.firestore
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var completeDateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        val nameEditText = findViewById<EditText>(R.id.edit_goal_name_edit_text)
        val amountEditText = findViewById<EditText>(R.id.edit_goal_amount_edit_text)
        val descriptionEditText = findViewById<EditText>(R.id.edit_goal_description_edit_text)
        val categoryEditText = findViewById<EditText>(R.id.edit_goal_category_edit_text)
        val savedAmountEditText = findViewById<EditText>(R.id.edit_goal_saved_amount_edit_text)
        completeDateEditText = findViewById(R.id.completeDateEditText)

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
                    completeDateEditText.setText(dateFormat.format(goal.completeDate))
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }

        completeDateEditText.setOnClickListener {
            showDatePicker()
        }

        val updateButton = findViewById<Button>(R.id.update_goal_button)
        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val savedAmount = savedAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val description = descriptionEditText.text.toString()
            val category = categoryEditText.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val completeDate = completeDateEditText.text.toString()

            val parsedCompleteDate = dateFormat.parse(completeDate)
            val completeDateValue = parsedCompleteDate ?: Date()

            if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields to update!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedGoal = Goal(userId, goalId, name, amount, savedAmount, description, category, completeDateValue)

            db.collection("goals")
                .document(goalId)
                .set(updatedGoal)
                .addOnSuccessListener {
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
                    Toast.makeText(this, "Goal Deleted successfully!", Toast.LENGTH_SHORT).
                    show()
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val selectedDate = calendar.time
        completeDateEditText.setText(dateFormat.format(selectedDate))
    }
}
