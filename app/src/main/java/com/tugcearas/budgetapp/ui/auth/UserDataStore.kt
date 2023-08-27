package com.tugcearas.budgetapp.ui.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data_store")

class UserDataStore(context: Context) {
    private val dataStore = context.dataStore

    val nameSurnameFlow: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.NAME_SURNAME]
        }

    suspend fun saveNameSurname(nameSurname: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME_SURNAME] = nameSurname
        }
    }

    private object PreferencesKeys {
        val NAME_SURNAME = stringPreferencesKey("name_surname")
    }
}
