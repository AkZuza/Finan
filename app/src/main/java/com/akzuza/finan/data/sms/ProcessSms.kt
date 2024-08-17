package com.akzuza.finan.data.sms

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Telephony
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.akzuza.finan.data.FinanTransaction
import com.akzuza.finan.data.FinanTransactionEvent
import com.akzuza.finan.data.TransactionType
import com.akzuza.finan.ui.home.FinanHomeState
import java.text.SimpleDateFormat
import java.util.Calendar

fun CheckAndUpsertNewTransactions(
    state: FinanHomeState,
    contentResolver: ContentResolver,
    onEvent: (FinanTransactionEvent) -> Unit
) {
    val uri: Uri = Telephony.Sms.Inbox.CONTENT_URI
    val projections = arrayOf(
        Telephony.Sms.Inbox._ID,
        Telephony.Sms.Inbox.BODY,
        Telephony.Sms.Inbox.DATE,
        Telephony.Sms.Inbox.ADDRESS
    )

    val selectionClause = "WHERE ${Telephony.Sms.BODY} HAS UPI"

    val random: Array<String> = emptyArray()

    val cursor = contentResolver.query(
        uri,
        projections,
        null,
        null,
        null
    )

    if(cursor == null)
        return

    if(cursor.moveToFirst()) {
        do {

            val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox.ADDRESS))
            val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox.BODY))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox.DATE))
            val id = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.Inbox._ID))

            if (body.contains("UPI")) {
                ProcessSms(id, address, body, date, state.lastdatetime, onEvent)
            }

        } while(cursor.moveToNext())
    }

    cursor.close()
}


fun ProcessSms(
    id: String,
    address: String,
    body: String,
    unixDate: String,
    lastDate: String,
    onEvent: (FinanTransactionEvent) -> Unit
) {

    val date = ConvertUnixToNormal(unixDate)
    if(lastDate.isNotBlank() && lastDate > date)
        return
    else if(date > lastDate)
        onEvent(FinanTransactionEvent.SetLastDateTime(date))

    var transaction = if (address.contains("HDFC")) ProccessHDFC(body, date)
                    else null

    if (transaction != null) {
        transaction = FinanTransaction(
            transaction.from, transaction.to, transaction.amount, transaction.type, transaction.datetime,
            id.toInt()
        )
    }

    if(transaction != null) {
        onEvent(FinanTransactionEvent.SetTransaction(transaction))
        onEvent(FinanTransactionEvent.AddTransaction)
    }
}

fun ConvertUnixToNormal(date: String): String {
    val timestampMillis = date.toLong()
    val timestampSeconds = timestampMillis / 1000

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestampSeconds * 1000 // Convert back to milliseconds

    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formattedDate = formatter.format(calendar.time)
    return formattedDate
}

fun ProccessHDFC(body: String, date: String): FinanTransaction? {
    val sendingMoney = body.contains("Sent")
    val recievingMoney = body.contains("credited")

    if(sendingMoney == false && recievingMoney == false) return null

    val regex = Regex(
        if(sendingMoney) "Amt Sent Rs\\.(\\d+)\\.\\d{2}\\s*From\\s*(.*?)\\s*To\\s*(.*)"
        else if(recievingMoney) "HDFC Bank: Rs\\. (\\d+)\\.\\d{2} credited to a/c (\\w+) on \\d{2}-\\d{2}-\\d{2} by a/c linked to VPA (\\w+@\\w+)"
        else ""
    )
    val matchResult = regex.find(body) ?: return null

    val amount = matchResult.groupValues[1].toInt()
    var from = matchResult.groupValues[2]
    var to = matchResult.groupValues[3]

    if(recievingMoney) {
        val temp = from
        from = to
        to = temp
    }

    return FinanTransaction(
        from = from,
        to = to,
        datetime = date,
        type = TransactionType.UPI,
        amount = amount
    )
}

@Composable
fun CheckAndRequestForSMSPermission() {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { result ->
        hasPermission = result
    }

    if(!hasPermission) {
        LaunchedEffect(key1 = "app_start") {
            launcher.launch(Manifest.permission.READ_SMS)
        }
    }
}

