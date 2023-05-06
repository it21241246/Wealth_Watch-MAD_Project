package com.example.mad_project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ViewPage : AppCompatActivity() {

    private lateinit var totalExpensesTextView: TextView
    private lateinit var totalIncomesTextView: TextView

    @SuppressLint("WrongViewCast")
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



        totalExpensesTextView = findViewById(R.id.totalExpensesTextView)
        totalIncomesTextView = findViewById(R.id.totalIncomesTextView)

        getTotalIncomeAndExpensesForCurrentMonth()

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@ViewPage, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@ViewPage, HomePageActivity::class.java)
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

    fun getTotalIncomeAndExpensesForCurrentMonth() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Add 1 since month is 0-indexed

        // Query to get all expenses for current user within current month
        db.collection("expenses")
            .whereEqualTo("userid", currentUser?.uid)
            .get()
            .addOnSuccessListener { expenses ->
                var totalExpenses = 0.0
                for (doc in expenses) {
                    val amount = doc.getDouble("amount") ?: 0.0
                    val date = doc.getTimestamp("date")
                    if (date != null) {
                        val calendar = Calendar.getInstance()
                        calendar.time = date.toDate()
                        val month = calendar.get(Calendar.MONTH) + 1 // Add 1 since month is 0-indexed
                        if (month == currentMonth) {
                            totalExpenses += amount
                        }
                    }
                }

                // Query to get all income for current user within current month
                db.collection("incomes")
                    .whereEqualTo("userid", currentUser?.uid)
                    .get()
                    .addOnSuccessListener { income ->
                        var totalIncome = 0.0
                        for (doc in income) {
                            val amount = doc.getDouble("amount") ?: 0.0
                            val date = doc.getTimestamp("date")
                            if (date != null) {
                                val calendar = Calendar.getInstance()
                                calendar.time = date.toDate()
                                val month = calendar.get(Calendar.MONTH) + 1 // Add 1 since month is 0-indexed
                                if (month == currentMonth) {
                                    totalIncome += amount
                                }
                            }
                        }

                        // Display the total income and expenses for the current month
                        totalIncomesTextView.text = String.format("%.2f", totalIncome)
                        totalExpensesTextView.text = String.format("%.2f", totalExpenses)
                    }
            }
    }
}