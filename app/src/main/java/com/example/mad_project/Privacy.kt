package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mad_project.databinding.ActivityPrivacyBinding

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

    }
}