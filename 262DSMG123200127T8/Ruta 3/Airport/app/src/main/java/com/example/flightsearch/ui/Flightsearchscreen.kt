package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.Airport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Flight Search") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Aeropuerto o código IATA") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar búsqueda")
                        }
                    }
                }
            )

            when (val c = content) {
                is SearchContent.Favorites -> FavoritesList(
                    favorites = c.items,
                    onToggle = { dep, dest -> viewModel.toggleFavorite(dep, dest, true) }
                )

                is SearchContent.Suggestions -> SuggestionsList(
                    airports = c.airports,
                    onSelect = {
                        focusManager.clearFocus()
                        viewModel.onAirportSelected(it)
                    }
                )

                is SearchContent.Flights -> FlightsList(
                    departure = c.departure,
                    items = c.items,
                    onToggle = { dest, isFav ->
                        viewModel.toggleFavorite(c.departure.iataCode, dest, isFav)
                    }
                )
            }
        }
    }
}

@Composable
private fun SuggestionsList(airports: List<Airport>, onSelect: (Airport) -> Unit) {
    if (airports.isEmpty()) {
        Text(
            "Sin resultados",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        return
    }
    LazyColumn {
        items(airports, key = { it.id }) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(airport) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(airport.iataCode, fontWeight = FontWeight.Bold)
                Text(airport.name)
            }
        }
    }
}

@Composable
private fun FlightsList(
    departure: Airport,
    items: List<FlightItem>,
    onToggle: (destinationCode: String, isFavorite: Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Vuelos desde ${departure.iataCode}",
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(items, key = { it.destination.id }) { flight ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("SALIDA", style = MaterialTheme.typography.labelSmall)
                        AirportLine(departure.iataCode, departure.name)
                        Text(
                            "LLEGADA",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        AirportLine(flight.destination.iataCode, flight.destination.name)
                    }
                    FavoriteStar(isFavorite = flight.isFavorite) {
                        onToggle(flight.destination.iataCode, flight.isFavorite)
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesList(
    favorites: List<com.example.flightsearch.data.Favorite>,
    onToggle: (departure: String, destination: String) -> Unit
) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Rutas favoritas", style = MaterialTheme.typography.titleMedium)
        }
        if (favorites.isEmpty()) {
            item {
                Text(
                    "Aún no tienes rutas favoritas. Busca un aeropuerto y toca la estrella.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        items(favorites, key = { it.id }) { fav ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${fav.departureCode}  →  ${fav.destinationCode}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    FavoriteStar(isFavorite = true) {
                        onToggle(fav.departureCode, fav.destinationCode)
                    }
                }
            }
        }
    }
}

@Composable
private fun AirportLine(code: String, name: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(code, fontWeight = FontWeight.Bold)
        Text(name)
    }
}

@Composable
private fun FavoriteStar(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Guardar como favorito",
            tint = if (isFavorite) Color(0xFFFFB300) else MaterialTheme.colorScheme.outline
        )
    }
}