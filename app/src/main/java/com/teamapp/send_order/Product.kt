package com.teamapp.send_order

class Product(
    val name: String,
    val price: Double = 12.0,
    var quantity: Int = 1,
    var isChecked: Boolean = false
){
    constructor(): this("", 0.0, 1, false)
}