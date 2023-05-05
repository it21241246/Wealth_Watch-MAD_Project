package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.navigation.NavigationBarView

class ViewPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_page)

        val viewexpenses: Button = findViewById(R.id.viewExpenses)
        viewexpenses.setOnClickListener {
            val intent = Intent(this, DisplayExpenses::class.java)
            startActivity(intent)
        }
        val viewincomes: Button = findViewById(R.id.viewIncomes)
        viewincomes.setOnClickListener {
            val intent = Intent(this, DisplayIncome::class.java)
            startActivity(intent)
        }

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@ViewPage, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@ViewPage, profile::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@ViewPage, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@ViewPage, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })

    }
}