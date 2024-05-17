package com.teamapp.send_order

class Product(
    val name: String,
    val price: Double = 12.0,
    val quantity: Int = 1,
    var isChecked: Boolean = false
)