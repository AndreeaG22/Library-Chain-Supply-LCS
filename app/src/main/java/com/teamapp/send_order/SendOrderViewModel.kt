package com.teamapp.send_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.teamapp.ReceiptData

class SendOrderViewModel : ViewModel() {
    val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> = _products

    fun sendOrder(products: List<Product>, totalPrice: Double,address: String, email: String, date: String) {
        // Send order to the database
        val dataBase = Firebase.database
        val myRef = dataBase.getReference("orders")

        val order = ReceiptData(products, totalPrice, date, address, email)
        val newOrderRef = myRef.push()
        newOrderRef.setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Order sent successfully.")
            } else {
                println("Failed to send order: ${task.exception?.message}")
            }
        }
    }

    fun updateQty(item: Product, qty: Int) {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null && product.name == item.name) {
                        product.quantity -= qty
                        productSnapshot.ref.setValue(product)
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