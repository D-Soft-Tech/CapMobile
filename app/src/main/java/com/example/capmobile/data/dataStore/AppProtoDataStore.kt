package com.example.capmobile.data.dataStore

import androidx.datastore.core.DataStore
import com.example.capmobile.SavedUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class AppProtoDataStore(
    private val protoDataStore: DataStore<SavedUser>
) {

    val readProto: Flow<SavedUser> = protoDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(SavedUser.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateSavedUser(userName: String, password: String) {
        protoDataStore.updateData { preferences ->
            preferences.toBuilder().setUserName(userName).setPassword(password).build()
        }
    }
}