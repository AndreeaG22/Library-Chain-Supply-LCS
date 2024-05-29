package com.teamapp.employee_management

class Employee(val email: String, val name: String, val code: String, val role: String, val phone: String, val date: String) {
    constructor() : this("", "", "", "", "", "")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (email != other.email) return false
        if (name != other.name) return false
        if (code != other.code) return false
        if (role != other.role) return false
        if (phone != other.phone) return false
        if (date != other.date) return false

        return true
    }
}