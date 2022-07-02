package com.artsy.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Class that handles saving and retrieving []
 */
private const val FAVORITE_PREFERENCES_NAME = "favorite_preferences"

// Create a DataStore instance using the preferencesDataStore delegate (with Context as receiver)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = FAVORITE_PREFERENCES_NAME
)

class SettingsDataStore(preference_datastore: DataStore<Preferences>) {

    private val FAVORITE_ARTIST = stringSetPreferencesKey("favorite_artist")

    val preferenceFlow: Flow<Set<String>> = preference_datastore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // Default key-value
            preferences[FAVORITE_ARTIST] ?: setOf<String>()
        }

    suspend fun saveFavoriteToPreferencesStore(favoriteArtist: Set<String>, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_ARTIST] = favoriteArtist
        }
    }

}