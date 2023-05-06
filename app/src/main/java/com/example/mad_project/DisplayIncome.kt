package com.example.mad_project

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DisplayIncome : AppCompatActivity() {

    private lateinit var IncomeAdapter: IncomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_income)

        IncomeAdapter = IncomeAdapter(this)

        findViewById<RecyclerView>(R.id.expensesRecycler).apply {
            layoutManager = LinearLayoutManager(this@DisplayIncome)
            adapter = IncomeAdapter
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            loadExpenses(userId)
        }
    }

    private fun loadExpenses(userId: String) {
        FirebaseFirestore.getInstance()
            .collection("incomes")
            .whereEqualTo("userid", userId)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val dsList = queryDocumentSnapshots.documents
                val incomeList = mutableListOf<Income>()
                for (ds in dsList) {
                    val incomes = ds.toObject(Income::class.java)
                    incomes?.let { incomeList.add(it) }
                }
                IncomeAdapter.setIncome(incomeList)
            }
        val nav: NavigationBarView = findViewById(R.id.navbar)

        nav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {

                    R.id.home -> {
                        val intent = Intent(this@DisplayIncome, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.goals -> {
                        val intent = Intent(this@DisplayIncome, HomePageActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.stats -> {
                        val intent = Intent(this@DisplayIncome, ViewPage::class.java)
                        startActivity(intent)
                    }

                    R.id.settings -> {
                        val intent = Intent(this@DisplayIncome, profile::class.java)
                        startActivity(intent)
                    }

                }

                return true
            }
        })

    }
}

