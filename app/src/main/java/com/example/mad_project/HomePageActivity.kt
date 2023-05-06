package com.example.mad_project


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.navigation.NavigationBarView


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

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@HomePageActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@HomePageActivity, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@HomePageActivity, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@HomePageActivity, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })
    }
}
