package com.keystone.capmobile.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("preferences")
    private val appPreferencesManager = appContext.datastore

    suspend fun saveStringToDataStore(valueToBeSaved: String, key: String) {
        val keyUsedToSave = stringPreferencesKey(key)
        appPreferencesManager.edit { pref ->
            pref[keyUsedToSave] = valueToBeSaved
        }
    }

    fun readStringFromDataStore(key: String) = appPreferencesManager.data.map { pref ->
        pref[stringPreferencesKey(key)] ?: "not found"
    }

    suspend fun saveToken(token: String) {
        val keyUsedToSaveToken = stringPreferencesKey("token")
        appPreferencesManager.edit { pref ->
            pref[keyUsedToSaveToken] = token
        }
    }

    fun getToken() = appPreferencesManager.data.map { pref ->
        pref[stringPreferencesKey("token")] ?: "not found"
    }

    suspend fun saveIntToStore(key: String, valueToBeSaved: Int) {
        val keyUsedToSave = intPreferencesKey(key)
        appPreferencesManager.edit { pref ->
            pref[keyUsedToSave] = valueToBeSaved
        }
    }

    fun readIntFromDataStore(key: String) =
        appPreferencesManager.data.map { pref ->
            pref[intPreferencesKey(key)] ?: -1
        }

    suspend fun saveBooleanToStore(key: String, valueToBeSaved: Boolean) {
        val keyUsedToSave = booleanPreferencesKey(key)
        appPreferencesManager.edit { pref ->
            pref[keyUsedToSave] = valueToBeSaved
        }
    }

    fun readBooleanFromDataStore(key: String) =
        appPreferencesManager.data.map { pref ->
            pref[booleanPreferencesKey(key)] ?: false
        }
}