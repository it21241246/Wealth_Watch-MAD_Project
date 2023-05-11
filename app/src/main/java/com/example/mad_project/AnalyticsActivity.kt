package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import android.view.View
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

class AnalyticsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var selectedGoal: Goal

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@AnalyticsActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@AnalyticsActivity, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@AnalyticsActivity, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@AnalyticsActivity, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })

        val goalSpinner = findViewById<Spinner>(R.id.goal_spinner)
        val savedAmountTextView = findViewById<TextView>(R.id.saved_amount_text_view)
        val toBeSavedTextView = findViewById<TextView>(R.id.to_be_saved_text_view)
        val remainingDaysTextView = findViewById<TextView>(R.id.remaining_days_text_view)
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Get goals from Firebase Firestore and populate the spinner
        db.collection("goals")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val goals = result.toObjects(Goal::class.java)
                val goalNames = goals.map { it.name }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, goalNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                goalSpinner.adapter = adapter

                // Set the initial selected goal
                if (goals.isNotEmpty()) {
                    selectedGoal = goals.first()
                    updateGoalDetails()
                }

                goalSpinner.onItemSelectedListener = this
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val goalName = parent.getItemAtPosition(position) as String

        // Retrieve the selected goal from Firebase Firestore
        db.collection("goals")
            .whereEqualTo("name", goalName)
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    selectedGoal = result.documents.first().toObject<Goal>()!!
                    updateGoalDetails()
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Do nothing
    }

    private fun updateGoalDetails() {
        val savedAmountTextView = findViewById<TextView>(R.id.saved_amount_text_view)
        val toBesavedTextView = findViewById<TextView>(R.id.to_be_saved_text_view)
        val remainingDaysTextView = findViewById<TextView>(R.id.remaining_days_text_view)

        savedAmountTextView.text = "Amount saved so far: ${selectedGoal.savedAmount}"
        val toBeSaved = selectedGoal.amount - selectedGoal.savedAmount
        toBesavedTextView.text = "Amount yet to be saved: $toBeSaved"

        // Calculate remaining days
        val currentDate = Calendar.getInstance().time
        val completeDate = selectedGoal.completeDate
        val remainingDays = calculateRemainingDays(currentDate, completeDate)
        remainingDaysTextView.text = "Remaining days: $remainingDays"
    }
    private fun calculateRemainingDays(currentDate: Date, completeDate: Date): Int {
        // Set the time to 00:00:00 for both dates to remove the effect of time
        val currentCal = Calendar.getInstance().apply {
            time = currentDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val completeCal = Calendar.getInstance().apply {
            time = completeDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Calculate the difference in days
        val diffInMillis = completeCal.timeInMillis - currentCal.timeInMillis
        val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)

        // Add 1 to include the completeDate as well
        return max(diffInDays.toInt() + 1, 0)
    }
}




