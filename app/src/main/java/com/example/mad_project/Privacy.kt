package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.mad_project.databinding.ActivityPrivacyBinding
import com.google.android.material.navigation.NavigationBarView


class Privacy : AppCompatActivity() {

    private lateinit var binding : ActivityPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deleteButton.setOnClickListener {
            startActivity(
                Intent(this, deleteAccount::class.java)
            )
        }

        binding.changePassword.setOnClickListener {
            startActivity(
                Intent(this, ChangePassword::class.java)
            )
        }
        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@Privacy, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@Privacy, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@Privacy, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@Privacy, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })

    }
}