package com.example.homesecurity.ui.record

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RecordData
import com.example.homesecurity.ui.home.getCurrentUserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.suspendCoroutine

class RecordViewModel : ViewModel()  {

    private val _records = MutableStateFlow<List<RecordData>?>(null)
    val records = _records.asStateFlow()

    suspend fun listRecords(): List<RecordData> {
        val id = getCurrentUserId()
        Log.i("Record", "ID ottenuto: $id")
        if (id.isEmpty()) {
            Log.e("Record", "ID utente nullo o vuoto")
        }
        val recordList = mutableListOf<RecordData>() // Lista temporanea per memorizzare i dati

        return suspendCoroutine { _ ->
            Amplify.API.query(
                ModelQuery.list(RecordData::class.java, RecordData.USER.contains(id)),
                { response ->
                    response.data.forEach { record ->
                        recordList.add(record)
                        Log.i("Record","Record: $record")
                    }
                    _records.value = recordList
                },
                { Log.e("Record", "Query failure", it) }
            )
        }
    }
}