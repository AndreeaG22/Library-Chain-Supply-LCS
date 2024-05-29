package com.teamapp.employee_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class EmployeeManagementViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _employeeNames = MutableLiveData<List<String>>()
    val employeeNames: LiveData<List<String>> get() = _employeeNames

    fun iterateDatabaseItems(reference: DatabaseReference) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val namesList = mutableListOf<String>()

                // Iterate through all the children of the reference
                for (snapshot in dataSnapshot.children) {
                    // Get the value of each child
                    val value = snapshot.getValue(Employee::class.java)
                    // Add the employee name to the list if it's not null
                    value?.name?.let { namesList.add(it) }
                }

                // Update the LiveData with the new list of names
                _employeeNames.value = namesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
                println("Database error: ${databaseError.message}")
            }
        })
    }
}