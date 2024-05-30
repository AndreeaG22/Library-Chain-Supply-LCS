package com.teamapp.home

class OldLineData (
    val receiver: String,
    val code: String,
    val quantity: Int,
    val totalSum: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OldLineData

        if (receiver != other.receiver) return false
        if (code != other.code) return false
        if (quantity != other.quantity) return false
        if (totalSum != other.totalSum) return false

        return true
    }

    override fun hashCode(): Int {
        var result = receiver.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + quantity.hashCode()
        result = 31 * result + totalSum.hashCode()
        return result
    }
}