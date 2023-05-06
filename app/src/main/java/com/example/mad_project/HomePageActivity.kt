package com.example.mad_project


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button


class HomePageActivity : AppCompatActivity() {

    private lateinit var createGoalButton: Button
    private lateinit var viewGoalsButton: Button
    private lateinit var analyticsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_main)

        createGoalButton = findViewById(R.id.create_goal_button)
        viewGoalsButton = findViewById(R.id.view_goals_button)
        analyticsButton = findViewById(R.id.analytics_button)

        createGoalButton.setOnClickListener {
            // Start the activity for creating a new goal
            startActivity(Intent(this, CreateGoalActivity::class.java))
        }

        viewGoalsButton.setOnClickListener {
            // Start the activity for viewing current goals
            startActivity(Intent(this, CurrentGoalsActivity ::class.java))
        }

        analyticsButton.setOnClickListener {
            // Start the activity for viewing analytics
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }
    }
}
