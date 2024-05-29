package com.teamapp.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.teamapp.client_management.Client

class HomeViewModel : ViewModel() {
    val dataBase = Firebase.database
    val _futureOrders = MutableLiveData<List<FutureLineData>>()
    val myRef = dataBase.getReference("clients")
    val futureOrders: MutableLiveData<List<FutureLineData>> = _futureOrders

    init {
        getFutureOrders()
    }

//    private fun getSumProducts(products: List<Product>): Int {
//        val thisRef = dataBase.getReference("products")
//
//    }

    private fun getFutureOrders() {
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
                            0
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