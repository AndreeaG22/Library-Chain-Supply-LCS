package com.teamapp.home

class FutureLineData (
    val receiver: String,
    val date: String,
    val priority: Int,
    val totalSum: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FutureLineData

        if (receiver != other.receiver) return false
        if (date != other.date) return false
        if (priority != other.priority) return false
        if (totalSum != other.totalSum) return false

        return true
    }

    override fun hashCode(): Int {
        var result = receiver.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + totalSum
        return result
    }
}