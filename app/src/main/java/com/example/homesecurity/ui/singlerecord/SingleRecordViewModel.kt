package com.example.homesecurity.ui.singlerecord

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RecordData
import com.example.homesecurity.ui.home.getCurrentUserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.suspendCoroutine

class SingleRecordViewModel : ViewModel()  {

    private val _record = MutableStateFlow<RecordData?>(null)
    val record = _record.asStateFlow()

    suspend fun getRecord(timestamp: String): RecordData? {
        val id = getCurrentUserId()
        Log.i("Record", "ID ottenuto: $id")
        if (id.isEmpty()) {
            Log.e("Record", "ID utente nullo o vuoto")
            return null
        }

        return suspendCoroutine { continuation ->
            Amplify.API.query(
                ModelQuery.list(RecordData::class.java, RecordData.USER.contains(id)),
                { response ->
                    var foundRecord: RecordData? = null
                    response.data.forEach { record ->
                        if (record.timestamp == timestamp) {
                            foundRecord = record
                            Log.i("Record", "Record trovato: $record")
                            return@forEach
                        }
                    }

                    if (foundRecord != null) {
                        _record.value = foundRecord
                        continuation.resumeWith(Result.success(foundRecord))
                    } else {
                        Log.e("Record", "Nessun record trovato con il timestamp specificato")
                        continuation.resumeWith(Result.success(null))
                    }
                },
                { exception ->
                    Log.e("Record", "Query failure", exception)
                    continuation.resumeWith(Result.failure(exception))
                }
            )
        }
    }
}
