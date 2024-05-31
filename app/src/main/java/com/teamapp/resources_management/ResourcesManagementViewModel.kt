package com.teamapp.resources_management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teamapp.send_order.Product

class ResourcesManagementViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> = _products

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Products")

    init {
        fetchProducts()
    }

    fun supplyResource(name: String, qty: Int) {
        val productRef = myRef.child(name)

        productRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val existingQty = snapshot.child("quantity").getValue(Int::class.java) ?: 0
                    val newQty = existingQty + qty
                    productRef.child("quantity").setValue(newQty)
                } else {
                    val product = Product(name = name, quantity = qty)
                    productRef.setValue(product)
                }
                fetchProducts()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun fetchProducts() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }
                _products.value = productList
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    fun addResource(name: String, qty: Int, price: Double) {
        // add resource to database
        val product = Product(name = name, quantity = qty, price = price)
        myRef.child(name).setValue(product)
        fetchProducts()
    }
}
