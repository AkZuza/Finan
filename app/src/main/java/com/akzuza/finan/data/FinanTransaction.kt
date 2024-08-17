package com.akzuza.finan.data

import androidx.room.Entity
import androidx.room.PrimaryKey

object TransactionType {
    const val UPI = "UPI"
    const val CASH = "Cash"
}

@Entity(tableName = "transactions")
data class FinanTransaction(
    val from: String,
    val to: String,
    val amount: Int,
    val type: String,
    val datetime: String,
    @PrimaryKey
    val id: Int = 0
) {
    override operator fun equals(other: Any?): Boolean {
        return when(other) {
            is FinanTransaction -> {
                (
                        type == other.type &&
                                id == other.id &&
                                datetime == other.datetime
                        )
            }

            else -> false
        }
    }
}
