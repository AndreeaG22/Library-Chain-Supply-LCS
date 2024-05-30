package com.teamapp

import com.teamapp.send_order.Product

class ReceiptData(
    val products: List<Product>,
    val totalPrice: Double,
    val date: String,
    val destination: String,
    val email: String) {
    constructor() : this(emptyList(), 0.0, "", "", "")

    fun equals(other: ReceiptData): Boolean {
        return this.products == other.products &&
            this.totalPrice == other.totalPrice &&
            this.date == other.date &&
            this.destination == other.destination &&
            this.email == other.email
    }
}