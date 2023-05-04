package com.example.mad_project

import Expenses
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_project.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Attributes.Name

class ExpensesAdapter(private val context: Context) : RecyclerView.Adapter<ExpensesAdapter.MyViewHolder>() {
    private val expensesList: MutableList<Expenses>


    init {
        expensesList = ArrayList()
    }

    fun add(expenses: Expenses) {
        expensesList.add(expenses)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.expenses_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expenses = expensesList[position]
        holder.name.text = expenses.name
        holder.amount.text = expenses.amount.toString()

        // Convert Timestamp to Date
        val date = expenses.date?.toDate()

        // Format the Date to a String
        if (date != null) {
            holder.date.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        } else {
            holder.date.text = ""
        }

        holder.deleteButton.setOnClickListener {
            val expenseId = expenses.id
            val db = FirebaseFirestore.getInstance()
            val expensesCollectionRef = db.collection("expenses")
            val expenseDocRef = expenseId?.let { it1 -> expensesCollectionRef.document(it1) }
            if (expenseDocRef != null) {
                expenseDocRef.delete()
                    .addOnSuccessListener {
                        // Show a success message if the expense is deleted
                        Toast.makeText(holder.itemView.context, "Expense deleted", Toast.LENGTH_SHORT)
                            .show()
                        expensesList.removeAt(holder.adapterPosition)
                        notifyItemRemoved(holder.adapterPosition)

                    }
                    .addOnFailureListener { e ->
                        // Show an error message if the expense couldn't be deleted
                        Toast.makeText(
                            holder.itemView.context,
                            "Error deleting expense: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        holder.editButton.setOnClickListener {
            val expense = expensesList[holder.adapterPosition]
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Edit User Details")

            // Create the layout for the dialog
            val view = LayoutInflater.from(holder.itemView.context).inflate(R.layout.update_popup, null)
            builder.setView(view)

            // Set the initial values for the input fields
            view.findViewById<EditText>(R.id.textname).setText(expense.name)
            view.findViewById<EditText>(R.id.textamount).setText(expense.amount.toString())


            // Set up the save button
            builder.setPositiveButton("Save") { _, _ ->
                // Get the updated values from the input fields
                val updatedName = view.findViewById<EditText>(R.id.textname).text.toString()
                val updatedAmountStr = view.findViewById<EditText>(R.id.textamount).text.toString()
                val updatedAmount = updatedAmountStr.toDouble()


                // Update the user's details in the database
                val expenseId = expenses.id
                val db = FirebaseFirestore.getInstance()
                val expensesCollectionRef = db.collection("expenses")
                val expenseDocRef = expenseId?.let { it1 -> expensesCollectionRef.document(it1) }
                if (expenseDocRef != null) {
                    expenseDocRef.update(mapOf(
                        "name" to updatedName,
                        "amount" to updatedAmount,

                        ))
                        .addOnSuccessListener {
                            // Show a success message if the user's details are updated
                            Toast.makeText(holder.itemView.context, "User details updated", Toast.LENGTH_SHORT).show()

                            // Update the user's details in the local list and refresh the adapter
                            expense.name = updatedName
                            expense.amount = updatedAmount

                            notifyItemChanged(holder.adapterPosition)
                        }
                        .addOnFailureListener { e ->
                            // Show an error message if the user's details couldn't be updated
                            Toast.makeText(holder.itemView.context, "Error updating user details: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            // Set up the cancel button
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            // Show the dialog
            val dialog = builder.create()
            dialog.show()
        }

    }


    override fun getItemCount(): Int {
        return expensesList.size
    }

    fun setExpenses(expensesList: List<Expenses>) {
        this.expensesList.clear()
        this.expensesList.addAll(expensesList)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        val editButton: Button = itemView.findViewById(R.id.edit_button)

    }


}
