package com.teamapp.receipt_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.teamapp.ReceiptData

class ReceiptHistoryViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _receipts = MutableLiveData<List<ReceiptData>>()
    val receipts: LiveData<List<ReceiptData>> get() = _receipts

    fun getReceipts() {
        // get receipts from database
        val db = Firebase.database
        val ref = db.getReference("orders")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val receiptsList = mutableListOf<ReceiptData>()

                for (snapshot in dataSnapshot.children) {
                    val receipt = snapshot.getValue(ReceiptData::class.java)
                    receipt?.let { receiptsList.add(it) }
                }

                _receipts.value = receiptsList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })
    }
}