package com.teamapp.client_management

class Product(
    val name: String,
    val price: Double = 0.0,
    var isChecked: Boolean = false
) {
    constructor() : this("", 0.0, false)
}