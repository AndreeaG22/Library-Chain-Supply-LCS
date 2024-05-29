package com.teamapp.client_management

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AddClientViewModel : ViewModel() {
    fun addClient(name: String, email: String, dateDelivery: String, products: List<Product>) {
        // Add client to the database
        val client = Client(name, email, dateDelivery, products)
        val dataBase = Firebase.database
        val myRef = dataBase.getReference("clients")

        val query = myRef.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Employee with this email already exists
                    println("Employee with email $email already exists.")
                } else {
                    // Employee with this email does not exist, add the new employee
                    val newEmployeeRef = myRef.push()
                    newEmployeeRef.setValue(client).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("Employee added successfully.")
                        } else {
                            println("Failed to add employee: ${task.exception?.message}")
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })
    }


}