package com.example.spainguide.ui

import androidx.lifecycle.ViewModel
import com.example.spainguide.data.LocalData
import com.example.spainguide.model.Category
import com.example.spainguide.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SpainUiState(
    val currentCategory: Category = LocalData.categories.first(),
    val categoryPlaces: List<Place> = LocalData.places.filter { it.categoryId == LocalData.categories.first().id },
    val currentPlace: Place = LocalData.places.first()
)

class SpainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SpainUiState())
    val uiState: StateFlow<SpainUiState> = _uiState


    fun updateCurrentCategory(category: Category) {
        val placesForCategory = LocalData.places.filter { it.categoryId == category.id }
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category,
                categoryPlaces = placesForCategory,
                currentPlace = placesForCategory.first()
            )
        }
    }

    fun updateCurrentPlace(place: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                currentPlace = place
            )
        }
    }
}