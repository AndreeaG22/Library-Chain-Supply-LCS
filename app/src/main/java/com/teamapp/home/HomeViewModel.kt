package com.teamapp.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.teamapp.ReceiptData
import com.teamapp.client_management.Client
import com.teamapp.client_management.Product

class HomeViewModel : ViewModel() {
    val dataBase = Firebase.database
    val _futureOrders = MutableLiveData<List<FutureLineData>>()
    val futureOrders: MutableLiveData<List<FutureLineData>> = _futureOrders
    val _oldOrders = MutableLiveData<List<OldLineData>>()
    val oldOrders: MutableLiveData<List<OldLineData>> = _oldOrders

    init {
        getFutureOrders()
        getOldOrders()
    }

    private fun getSumProducts(products: List<Product>): Double {
        var sum = 0.0
        for (product in products) {
            sum += product.price
        }
        return sum

    }

    private fun getOldOrders() {
        val myRef = dataBase.getReference("orders")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val oldOrdersList = mutableListOf<OldLineData>()
                for (orderSnapshot in dataSnapshot.children) {
                    val order = orderSnapshot.getValue(ReceiptData::class.java)
                    order?.let {
                        oldOrdersList.add(OldLineData(
                            it.email,
                            it.getNumberReceipt(),
                            it.products.size,
                            it.totalPrice
                        ))
                    }
                }
                // sort the list by date
                oldOrdersList.sortBy { it.code }
                _oldOrders.value = oldOrdersList
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })
    }

    private fun getFutureOrders() {
        val myRef = dataBase.getReference("clients")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val futureOrdersList = mutableListOf<FutureLineData>()
                for (clientSnapshot in dataSnapshot.children) {
                    val client = clientSnapshot.getValue(Client::class.java)
                    client?.let {
                        futureOrdersList.add(FutureLineData(
                            it.address,
                            it.date_deliery,
                            it.address,
                            getSumProducts(it.products)
                        ))
                    }
                }
                // sort the list by date
                futureOrdersList.sortBy { it.date }
                _futureOrders.value = futureOrdersList
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })
    }
}