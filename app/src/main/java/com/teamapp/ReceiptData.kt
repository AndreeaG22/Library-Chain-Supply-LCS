package com.teamapp

import com.teamapp.send_order.Product

class ReceiptData(
    val products: List<Product>,
    val totalPrice: Double,
    val date: String,
    val destination: String,
    val email: String) {
    constructor() : this(emptyList(), 0.0, "", "", "")
    fun getNumberReceipt() : String{
       // create a cod wich contains the date of the order and the first 3 letters of the destination
        val date = date.split("-")
        return "${date[0]}${date[1]}${date[2].substring(2)}${destination.substring(0, 3)}"
    }
}