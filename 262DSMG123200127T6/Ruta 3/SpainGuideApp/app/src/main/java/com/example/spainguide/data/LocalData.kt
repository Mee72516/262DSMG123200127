package com.example.spainguide.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Restaurant
import com.example.spainguide.R
import com.example.spainguide.model.Category
import com.example.spainguide.model.CategoryAccent
import com.example.spainguide.model.Place

object LocalData {
    val categories = listOf(
        Category(
            id = 1,
            nameRes = R.string.category_cities,
            icon = Icons.Filled.LocationCity,
            accent = CategoryAccent.PRIMARY_CONTAINER
        ),
        Category(
            id = 2,
            nameRes = R.string.category_beaches,
            icon = Icons.Filled.BeachAccess,
            accent = CategoryAccent.SECONDARY_CONTAINER
        ),
        Category(
            id = 3,
            nameRes = R.string.category_food,
            icon = Icons.Filled.Restaurant,
            accent = CategoryAccent.TERTIARY_CONTAINER
        ),
        Category(
            id = 4,
            nameRes = R.string.category_activities,
            icon = Icons.Filled.LocalActivity,
            accent = CategoryAccent.PRIMARY_SOLID
        )
    )

    val places = listOf(
        // Ciudades Históricas
        Place(1, 1, R.string.place_madrid, R.string.desc_madrid, R.drawable.madrid),
        Place(2, 1, R.string.place_barcelona, R.string.desc_barcelona, R.drawable.barcelona),
        Place(3, 1, R.string.place_sevilla, R.string.desc_sevilla, R.drawable.sevilla),
        Place(4, 1, R.string.place_granada, R.string.desc_granada, R.drawable.granada),
        Place(5, 1, R.string.place_toledo, R.string.desc_toledo, R.drawable.toledo),

        // Playas y Costas
        Place(6, 2, R.string.place_ibiza, R.string.desc_ibiza, R.drawable.ibiza),
        Place(7, 2, R.string.place_mallorca, R.string.desc_mallorca, R.drawable.mallorca),
        Place(8, 2, R.string.place_costa_brava, R.string.desc_costa_brava, R.drawable.costa_brava),
        Place(9, 2, R.string.place_tenerife, R.string.desc_tenerife, R.drawable.tenerife),

        // Rutas Gastronómicas
        Place(10, 3, R.string.place_tapas, R.string.desc_tapas, R.drawable.ruta_tapas_andalucia),
        Place(11, 3, R.string.place_paella, R.string.desc_paella, R.drawable.paella_valencia),
        Place(12, 3, R.string.place_pintxos, R.string.desc_pintxos, R.drawable.pintxos_san_sebastian),
        Place(13, 3, R.string.place_jamon, R.string.desc_jamon, R.drawable.ruta_jamon_iberico),

        // Actividades y Experiencias
        Place(14, 4, R.string.place_flamenco, R.string.desc_flamenco, R.drawable.noche_flamenco),
        Place(15, 4, R.string.place_camino, R.string.desc_camino, R.drawable.camino_santiago),
        Place(16, 4, R.string.place_alhambra, R.string.desc_alhambra, R.drawable.tour_alhambra),
        Place(17, 4, R.string.place_sanfermin, R.string.desc_sanfermin, R.drawable.san_fermin_pamplona)
    )
}