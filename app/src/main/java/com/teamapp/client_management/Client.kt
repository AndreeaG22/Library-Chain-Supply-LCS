package com.teamapp.client_management

class Client(
    val address: String,
    val email: String,
    val date_deliery: String,
    val products: List<Product>
) {
    constructor() : this("", "", "", emptyList())
}