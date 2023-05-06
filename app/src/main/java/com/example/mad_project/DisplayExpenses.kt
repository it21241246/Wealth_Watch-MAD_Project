package com.example.mad_project

import Expenses
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_project.R
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
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

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            loadExpenses(userId)
        }
    }

    private fun loadExpenses(userId: String) {
        FirebaseFirestore.getInstance()
            .collection("expenses")
            .whereEqualTo("userid", userId)
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

        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@DisplayExpenses, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@DisplayExpenses, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@DisplayExpenses, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@DisplayExpenses, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })

    }




}

