package com.akzuza.finan.ui.home

import android.app.Application
import android.database.Cursor
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akzuza.finan.data.FinanTransaction
import com.akzuza.finan.data.FinanTransactionDao
import com.akzuza.finan.data.FinanTransactionEvent
import com.akzuza.finan.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinanHomeViewModel(
    private val transactionsDao: FinanTransactionDao
)
    : ViewModel() {

    private val _uiState = MutableStateFlow(FinanHomeState())
    val uiState: StateFlow<FinanHomeState> = _uiState

    init {
        observeAllTransactions()
    }

    private fun observeAllTransactions() {

        viewModelScope.launch {

            _uiState.update { it.copy(
                transactions = transactionsDao.getAll()
            ) }
        }
    }

    fun scanForNewSms() {

    }

    fun onEvent(event: FinanTransactionEvent) {
        when (event) {
            is FinanTransactionEvent.AddTransaction -> {

                val from = _uiState.value.from
                val to = _uiState.value.to
                val amount = _uiState.value.amount
                val type = _uiState.value.type
                val datetime = _uiState.value.datetime
                val id = _uiState.value.id

                if(from.isBlank() || to.isBlank() || amount == 0) {
                    return
                }

                viewModelScope.launch {
                    transactionsDao.insertTransactionSafe(
                        FinanTransaction(
                            from = from,
                            to = to,
                            amount = amount,
                            datetime = datetime,
                            type = type,
                            id = id
                        )
                    )
                }

                _uiState.update { it.copy(
                    from = "",
                    to = "",
                    amount = 0,
                    type = TransactionType.UPI,
                    lastdatetime = datetime

                ) }
            }
            is FinanTransactionEvent.HideDialog -> {
                _uiState.update { it.copy(
                    showTransactionDialog = false
                ) }
            }
            is FinanTransactionEvent.ShowDialog -> {
                _uiState.update { it.copy(
                    showTransactionDialog = true
                ) }
            }

            is FinanTransactionEvent.SetAmount -> {
                _uiState.update { it.copy(amount = event.amount) }
            }
            is FinanTransactionEvent.SetDateTime -> {
                _uiState.update { it.copy(datetime = event.datetime) }
            }
            is FinanTransactionEvent.SetFrom -> {
                _uiState.update { it.copy(from = event.from) }
            }
            is FinanTransactionEvent.SetTo -> {
                _uiState.update { it.copy(to = event.to) }
            }
            is FinanTransactionEvent.SetType -> {
                _uiState.update { it.copy(type = event.type) }
            }

            is FinanTransactionEvent.SetTransaction -> {
                val transaction = event.transaction
                _uiState.update {
                    it.copy(
                        amount = transaction.amount,
                        from = transaction.from,
                        to = transaction.to,
                        type = transaction.type,
                        datetime = transaction.datetime,
                        id = transaction.id
                    )
                }
            }

            is FinanTransactionEvent.SetLastDateTime -> {
                _uiState.update { it.copy(lastdatetime = event.lastdatetime) }
            }

            FinanTransactionEvent.StartRefreshTransactions -> {
                _uiState.update { it.copy(refreshing = true) }
            }
            FinanTransactionEvent.StopRefreshTransactions -> {
                _uiState.update { it.copy(refreshing = false) }
            }
        }
    }
}