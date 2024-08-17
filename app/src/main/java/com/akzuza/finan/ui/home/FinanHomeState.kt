package com.akzuza.finan.ui.home

import com.akzuza.finan.data.FinanTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class FinanHomeState(
    val loading: Boolean = false,
    val showTransactionDialog: Boolean = false,
    val transactions: Flow<List<FinanTransaction>> = flow { emit(emptyList()) },
    val refreshing: Boolean = false,

    val from: String = "",
    val to: String = "",
    val amount: Int = 0,
    val type: String = "UPI",
    val datetime: String = "",
    val id: Int = 0,

    val lastdatetime: String = ""
)