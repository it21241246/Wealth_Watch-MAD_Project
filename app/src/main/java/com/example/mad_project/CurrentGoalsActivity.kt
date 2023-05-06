package com.example.mad_project

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.firestore.FirebaseFirestore

class CurrentGoalsActivity : AppCompatActivity() {

    private lateinit var goalsListView: ListView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_goals)

        goalsListView = findViewById(R.id.goals_list_view)
        db = FirebaseFirestore.getInstance()

        // Get all current goals from Firestore
        db.collection("goals")
            .get()
            .addOnSuccessListener { documents ->
                // Convert each Firestore document to a Goal object
                val goals = documents.map { it.toObject(Goal::class.java) }

                // Display the list of goals in the ListView
                val adapter = ArrayAdapter(this, R.layout.custom_list_item, goals.map { it.name })
                goalsListView.adapter = adapter

                // Set a click listener on each item in the list to go to the edit goal page
                goalsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedGoal = goals[position]
                    val intent = Intent(this, EditGoalActivity::class.java)
                    intent.putExtra("GOAL_ID", selectedGoal.id)
                    startActivity(intent)
                }

                // Add styling to each row of the ListView
                for (i in 0 until goalsListView.childCount) {
                    val listItem = goalsListView.getChildAt(i)
                    val shape = GradientDrawable()
                    shape.shape = GradientDrawable.RECTANGLE
                    shape.cornerRadius = 15F
                    shape.setColor(getColor(R.color.goal_row_color))
                    listItem.background = shape
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }
}
