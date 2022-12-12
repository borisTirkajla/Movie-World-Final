package com.example.movieworld.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.example.movieworld.util.Constants.PREFERENCES_BACK_ONLINE
import com.example.movieworld.util.Constants.PREFERENCES_NAME
import com.example.movieworld.util.Constants.SELECTED_GENRE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context //DataStore uses context
) {
    private object PreferencesKeys {
        val selectedGenreId = preferencesKey<Int>(SELECTED_GENRE_ID)
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCES_NAME)

    suspend fun saveGenreType(selectedGenreId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedGenreId] = selectedGenreId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    fun readGenreType(): Flow<GenreType> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else throw exception
            }
            .map { preferences ->
                val selectedGenreId = preferences[PreferencesKeys.selectedGenreId] ?: 0
                GenreType(
                    selectedGenreId = selectedGenreId
                )
            }

    fun readBackOnline(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception
        }.map { preferences ->
            val backOnline = preferences[PreferencesKeys.backOnline] ?: false
            backOnline
        }

}

data class GenreType(
    val selectedGenreId: Int
)