package com.teamapp.send_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SendOrderViewModel : ViewModel() {
    val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> = _products

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