package com.example.mad_project

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.mad_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import java.text.SimpleDateFormat
import java.util.*

class AddExpensesActivity : AppCompatActivity() {

    private lateinit var expenseNameEditText: EditText
    private lateinit var expenseAmountEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var saveExpenseButton: Button






    private val db = FirebaseFirestore.getInstance()
    val expensesRef = db.collection("expenses")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expenses)

        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


        expenseNameEditText = findViewById(R.id.expenseNameInput)
        expenseAmountEditText = findViewById(R.id.expenseAmountInput)
        dateEditText = findViewById(R.id.expenseDateInput)
        timeEditText = findViewById(R.id.expenseTimeInput)
        saveExpenseButton = findViewById(R.id.saveExpenseButton)

        // Get the current date and time
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        dateEditText.setText(dateFormat.format(calendar.time))
        timeEditText.setText(timeFormat.format(calendar.time))

        // Set up click listeners for the date and time fields to allow the user to edit them
        dateEditText.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    dateEditText.setText(dateFormat.format(calendar.time))
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }

        timeEditText.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                    calendar.set(Calendar.MINUTE, selectedMinute)
                    timeEditText.setText(timeFormat.format(calendar.time))
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }



        saveExpenseButton.setOnClickListener {
            val expenseName = expenseNameEditText.text.toString().trim()
            val expenseAmount = expenseAmountEditText.text.toString().toDouble()
            val date = calendar.time
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            // Create a new expense document in the 'expenses' collection with a unique ID
            val expensesCollectionRef = db.collection("expenses")
            val newExpenseDocRef = expensesCollectionRef.document()
            val newExpenseId = newExpenseDocRef.id

            // Create a new expense document with the data and date, including the documentation data
            val expense = hashMapOf(
                "userid" to userId,
                "id" to newExpenseId,
                "name" to expenseName,
                "amount" to expenseAmount,
                "date" to date,
                // Add any additional documentation data here
            )

            // Add the expense to the 'expenses' collection in Firestore
            newExpenseDocRef.set(expense)
                .addOnSuccessListener {
                    // Show a success message and finish the activity
                    Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    // Show an error message if the expense couldn't be added
                    Toast.makeText(this, "Error adding expense: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
