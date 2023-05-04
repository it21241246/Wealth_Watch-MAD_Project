package com.example.mad_project

import Expenses
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DisplayExpenses : AppCompatActivity() {

    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_expenses)

        expensesAdapter = ExpensesAdapter(this)

        findViewById<RecyclerView>(R.id.expensesRecycler).apply {
            layoutManager = LinearLayoutManager(this@DisplayExpenses)
            adapter = expensesAdapter
        }

        loadExpenses()
    }

    private fun loadExpenses() {
        FirebaseFirestore.getInstance()
            .collection("expenses")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val dsList = queryDocumentSnapshots.documents
                val expensesList = mutableListOf<Expenses>()
                for (ds in dsList) {
                    val expenses = ds.toObject(Expenses::class.java)
                    expenses?.let { expensesList.add(it) }
                }
                expensesAdapter.setExpenses(expensesList)
            }
    }
}

