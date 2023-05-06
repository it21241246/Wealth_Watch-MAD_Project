package com.example.mad_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import android.view.View


class AnalyticsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var selectedGoal: Goal

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        val goalSpinner = findViewById<Spinner>(R.id.goal_spinner)
        val savedAmountTextView = findViewById<TextView>(R.id.saved_amount_text_view)
        val toBeSavedTextView = findViewById<TextView>(R.id.to_be_saved_text_view)

        // Get goals from Firebase Firestore and populate the spinner
        db.collection("goals")
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
                    savedAmountTextView.text = "Amount saved so far: ${selectedGoal.savedAmount}"
                    val toBeSaved = selectedGoal.amount - selectedGoal.savedAmount
                    toBeSavedTextView.text = "Amount yet to be saved: $toBeSaved"
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
                    val toBeSaved = selectedGoal.amount - selectedGoal.savedAmount
                    findViewById<TextView>(R.id.saved_amount_text_view).text =
                        "Amount saved so far: ${selectedGoal.savedAmount}"
                    findViewById<TextView>(R.id.to_be_saved_text_view).text =
                        "Amount yet to be saved: $toBeSaved"
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Do nothing
    }
}
