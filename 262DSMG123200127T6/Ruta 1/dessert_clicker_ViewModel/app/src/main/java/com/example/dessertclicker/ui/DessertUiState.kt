package com.example.dessertclicker.ui

data class DessertUiState(
    val currentDessertIndex: Int = 0,
    val dessertsSold: Int = 0,
    val revenue: Int = 0,
    val currentDessertPrice: Int,
    val currentDessertImageId: Int
)