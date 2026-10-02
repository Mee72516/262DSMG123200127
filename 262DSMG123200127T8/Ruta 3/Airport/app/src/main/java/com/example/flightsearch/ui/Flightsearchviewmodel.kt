package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightDao
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FlightItem(val destination: Airport, val isFavorite: Boolean)

sealed interface SearchContent {
    data class Favorites(val items: List<Favorite>) : SearchContent
    data class Suggestions(val airports: List<Airport>) : SearchContent
    data class Flights(val departure: Airport, val items: List<FlightItem>) : SearchContent
}

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModel(
    private val dao: FlightDao,
    private val preferences: UserPreferencesRepository
) : ViewModel() {

    // El texto va en su propio flujo para que el TextField responda sin retraso
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedAirport = MutableStateFlow<Airport?>(null)

    init {
        // Restaura el texto guardado con DataStore al abrir la app
        viewModelScope.launch {
            _query.value = preferences.searchQuery.first()
        }
    }

    val content: StateFlow<SearchContent> =
        combine(_query, _selectedAirport) { query, airport -> query to airport }
            .flatMapLatest { (query, airport) ->
                when {
                    airport != null -> combine(
                        dao.getDestinations(airport.iataCode),
                        dao.getFavorites()
                    ) { destinations, favorites ->
                        SearchContent.Flights(
                            departure = airport,
                            items = destinations.map { dest ->
                                FlightItem(
                                    destination = dest,
                                    isFavorite = favorites.any {
                                        it.departureCode == airport.iataCode &&
                                                it.destinationCode == dest.iataCode
                                    }
                                )
                            }
                        )
                    }

                    query.isBlank() -> dao.getFavorites().map { SearchContent.Favorites(it) }

                    else -> dao.searchAirports(query.trim()).map { SearchContent.Suggestions(it) }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchContent.Favorites(emptyList())
            )

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        _selectedAirport.value = null
        saveQuery(newQuery)
    }

    fun onAirportSelected(airport: Airport) {
        _selectedAirport.value = airport
        _query.value = airport.iataCode
        saveQuery(airport.iataCode)
    }

    fun toggleFavorite(departureCode: String, destinationCode: String, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                dao.deleteFavorite(departureCode, destinationCode)
            } else {
                dao.insertFavorite(
                    Favorite(departureCode = departureCode, destinationCode = destinationCode)
                )
            }
        }
    }

    private fun saveQuery(query: String) {
        viewModelScope.launch { preferences.saveSearchQuery(query) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as FlightSearchApplication
                FlightSearchViewModel(app.container.flightDao, app.container.preferences)
            }
        }
    }
}