package com.example.spainguide.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spainguide.data.LocalData

enum class SpainScreen(val title: String) {
    CategoryList("Descubre España"),
    PlaceList("Lugares Recomendados"),
    PlaceDetail("Detalles")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpainAppBar(
    currentScreen: SpainScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
        }
    )
}

@Composable
fun SpainApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: SpainViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SpainScreen.valueOf(
        backStackEntry?.destination?.route ?: SpainScreen.CategoryList.name
    )

    val isExpanded = windowSize == WindowWidthSizeClass.Expanded

    Scaffold(
        topBar = {
            SpainAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null && !isExpanded,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        if (isExpanded) {
            Row(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                LazyColumn(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    items(LocalData.categories) { category ->
                        CategoryItem(
                            category = category,
                            selected = uiState.currentCategory.id == category.id,
                            onClick = { viewModel.updateCurrentCategory(category) },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                LazyColumn(modifier = Modifier.weight(1.5f).padding(end = 8.dp)) {
                    items(uiState.categoryPlaces) { place ->
                        PlaceListItem(
                            place = place,
                            onClick = { viewModel.updateCurrentPlace(place) },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                PlaceDetailScreen(
                    place = uiState.currentPlace,
                    onBackPressed = { },
                    modifier = Modifier.weight(1.5f)
                )
            }
        } else {

            NavHost(
                navController = navController,
                startDestination = SpainScreen.CategoryList.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = SpainScreen.CategoryList.name) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(LocalData.categories) { category ->
                            CategoryItem(
                                category = category,
                                selected = false,
                                onClick = {
                                    viewModel.updateCurrentCategory(category)
                                    navController.navigate(SpainScreen.PlaceList.name)
                                },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
                composable(route = SpainScreen.PlaceList.name) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(uiState.categoryPlaces) { place ->
                            PlaceListItem(
                                place = place,
                                onClick = {
                                    viewModel.updateCurrentPlace(place)
                                    navController.navigate(SpainScreen.PlaceDetail.name)
                                },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
                composable(route = SpainScreen.PlaceDetail.name) {
                    PlaceDetailScreen(
                        place = uiState.currentPlace,
                        onBackPressed = { navController.navigateUp() },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}