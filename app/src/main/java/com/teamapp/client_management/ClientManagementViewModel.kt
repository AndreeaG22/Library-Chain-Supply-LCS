package com.teamapp.client_management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ClientManagementViewModel : ViewModel() {
    // create a function that returns a list of clients
    val dataBase = Firebase.database
    val _clients = MutableLiveData<List<Client>>()
    val myRef = dataBase.getReference("clients")
    val clients: MutableLiveData<List<Client>> = _clients

    init {
        fetchClients()
    }


    private fun fetchClients() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val clientList = mutableListOf<Client>()
                for (clientSnapshot in dataSnapshot.children) {
                    val client = clientSnapshot.getValue(Client::class.java)
                    client?.let {
                        clientList.add(it)
                    }
                }
                _clients.value = clientList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Database error: ${databaseError.message}")
            }
        })

    }
}