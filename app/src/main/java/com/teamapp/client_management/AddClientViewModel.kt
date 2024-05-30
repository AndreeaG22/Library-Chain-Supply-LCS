package com.teamapp.client_management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.teamapp.send_order.Product

class AddClientViewModel : ViewModel() {
    val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> = _products
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

    init {
        getProducts()
    }

    private fun getProducts() {
        // Get products from the database
        val dataBase = Firebase.database
        val myRef = dataBase.getReference("Products")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                _products.value = productList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })

    }


}