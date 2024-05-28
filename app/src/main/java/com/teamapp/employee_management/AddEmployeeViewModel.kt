package com.teamapp.employee_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddEmployeeViewModel : ViewModel() {

    private val _addEmployeeResult = MutableStateFlow<Pair<Boolean, String>?>(null)
    val addEmployeeResult: StateFlow<Pair<Boolean, String>?> get() = _addEmployeeResult

    fun addEmployee(
        email: String,
        name: String,
        code: String,
        role: String,
        phone: String,
        date: String
    ) {
        viewModelScope.launch {
            val employee = Employee(email, name, code, role, phone, date)

            val database = Firebase.database
            val myRef = database.getReference("employees")

            val query = myRef.orderByChild("email").equalTo(email)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        _addEmployeeResult.value = Pair(false, "Employee with email $email already exists.")
                    } else {
                        val newEmployeeRef = myRef.push()
                        newEmployeeRef.setValue(employee).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseAuth = Firebase.auth
                                firebaseAuth.createUserWithEmailAndPassword(email, code)
                                    .addOnCompleteListener { authTask ->
                                        if (authTask.isSuccessful) {
                                            _addEmployeeResult.value = Pair(true, "Employee added successfully.")
                                        } else {
                                            _addEmployeeResult.value = Pair(false, "Failed to add employee to auth database: ${authTask.exception?.message}")
                                        }
                                    }
                            } else {
                                _addEmployeeResult.value = Pair(false, "Failed to add employee: ${task.exception?.message}")
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    _addEmployeeResult.value = Pair(false, "Database error: ${databaseError.message}")
                }
            })
        }
    }
}
