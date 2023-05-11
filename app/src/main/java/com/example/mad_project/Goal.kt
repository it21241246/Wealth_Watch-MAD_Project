package com.example.mad_project

import com.google.firebase.firestore.DocumentId

data class Goal(

    var userId: String = "",
    @DocumentId val id: String? = null,
    val name: String = "",
    val amount: Double = 0.0,
    var savedAmount: Double = 0.0,
    val description: String = "",
    val category: String = ""
)
