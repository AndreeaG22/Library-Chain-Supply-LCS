package com.teamapp.client_management

import com.teamapp.send_order.Product

class Client(
    val address: String,
    val email: String,
    val date_deliery: String,
    val products: List<Product>
) {
    constructor() : this("", "", "", emptyList())
    // overwrite the == operator
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Client
        if (address != other.address) {
            return false
        }
        if (email != other.email) {
            return false
        }
        if (date_deliery != other.date_deliery) {
            return false
        }
        if (products != other.products) {
            return false
        }
        return true
    }
}