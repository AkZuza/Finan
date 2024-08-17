package com.akzuza.finan.data

sealed interface FinanTransactionEvent {
    object AddTransaction: FinanTransactionEvent
    object ShowDialog: FinanTransactionEvent
    object HideDialog: FinanTransactionEvent
    object StartRefreshTransactions: FinanTransactionEvent
    object StopRefreshTransactions: FinanTransactionEvent
    data class SetFrom(val from: String): FinanTransactionEvent
    data class SetTo(val to: String): FinanTransactionEvent
    data class SetAmount(val amount: Int): FinanTransactionEvent
    data class SetType(val type: String): FinanTransactionEvent
    data class SetDateTime(val datetime: String): FinanTransactionEvent
    data class SetTransaction(val transaction: FinanTransaction): FinanTransactionEvent
    data class SetLastDateTime(val lastdatetime: String): FinanTransactionEvent
}

