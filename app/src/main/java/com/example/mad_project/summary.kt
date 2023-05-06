package com.example.mad_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class summary : AppCompatActivity() {

    private lateinit var totalExpensesTextView: TextView
    private lateinit var totalIncomesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        totalExpensesTextView = findViewById(R.id.totalExpensesTextView)
        totalIncomesTextView = findViewById(R.id.totalIncomesTextView)

        getTotalIncomeAndExpensesForCurrentMonth()
    }

    // Function to retrieve the total income and expenses for the current month for the current user
    // Function to retrieve the total income and expenses for the current month for the current user
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


