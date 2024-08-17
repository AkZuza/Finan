package com.akzuza.finan.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanTransactionDao {

    @Query("SELECT * FROM transactions ORDER BY datetime DESC")
    fun getAll() : Flow<List<FinanTransaction>>

    @Query("SELECT * FROM transactions ORDER BY datetime DESC LIMIT 1")
    fun getLastUPITransaction() : FinanTransaction

    @Query("SELECT * FROM transactions WHERE id=:id")
    fun getTransactionWithId(id: Int): FinanTransaction?

    @Upsert
    suspend fun upsertTransaction(finanTransaction: FinanTransaction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(finanTransaction: FinanTransaction)

    @Delete
    suspend fun deleteTransaction(finanTransaction: FinanTransaction)

    @Transaction
    suspend fun insertTransactionSafe(transaction: FinanTransaction) {
        val searchedTransaction = getTransactionWithId(transaction.id)
        if (searchedTransaction != null) {
            if (searchedTransaction.type != transaction.type) {
                var tempId = searchedTransaction.id
                while ( true ) {
                    if(getTransactionWithId(tempId) == null)
                        break
                    tempId++
                }

                val newTransaction = FinanTransaction(
                    from = transaction.from,
                    to = transaction.to,
                    amount = transaction.amount,
                    type = transaction.type,
                    id = tempId,
                    datetime = transaction.datetime
                )

                insertTransaction(newTransaction)
            }
        } else insertTransaction(transaction)
    }


}