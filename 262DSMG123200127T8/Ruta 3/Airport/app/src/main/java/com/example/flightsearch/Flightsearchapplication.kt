package com.example.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearch.data.FlightDao
import com.example.flightsearch.data.FlightDatabase
import com.example.flightsearch.data.UserPreferencesRepository

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class AppContainer(context: Context) {
    val flightDao: FlightDao = FlightDatabase.getDatabase(context).flightDao()
    val preferences = UserPreferencesRepository(context.dataStore)
}

class FlightSearchApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}