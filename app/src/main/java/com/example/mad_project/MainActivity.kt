package com.example.mad_project


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mad_project.R
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import org.checkerframework.checker.nullness.qual.NonNull


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@MainActivity, profile::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@MainActivity, profile::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@MainActivity, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@MainActivity, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })

        val addIncomeButton: Button = findViewById(R.id.addIncomeButton)
        addIncomeButton.setOnClickListener {
            val intent = Intent(this, AddIncomesActivity::class.java)
            startActivity(intent)
        }



        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)
        addExpenseButton.setOnClickListener {
            val intent = Intent(this, AddExpensesActivity::class.java)
            startActivity(intent)
        }




    }
}




