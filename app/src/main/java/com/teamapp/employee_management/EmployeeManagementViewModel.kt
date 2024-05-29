package com.teamapp.employee_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class EmployeeManagementViewModel : ViewModel() {
    private val _employees = MutableLiveData<List<Employee>>()
    val employees: LiveData<List<Employee>> get() = _employees

    fun iterateDatabaseItems(reference: DatabaseReference) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val employeesList = mutableListOf<Employee>()

                // Iterate through all the children of the reference
                for (snapshot in dataSnapshot.children) {
                    // Get the value of each child
                    val employee = snapshot.getValue(Employee::class.java)
                    // Add the employee to the list if it's not null
                    employee?.let { employeesList.add(it) }
                }

                // Update the LiveData with the new list of employees
                _employees.value = employeesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
                println("Database error: ${databaseError.message}")
            }
        })
    }

    fun deleteEmployee(reference: DatabaseReference, employee: Employee) {
        reference.child("employees").orderByChild("email").equalTo(employee.email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (employeeSnapshot in snapshot.children) {
                    employeeSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error deleting employee: ${error.message}")
            }
        })
    }
}
