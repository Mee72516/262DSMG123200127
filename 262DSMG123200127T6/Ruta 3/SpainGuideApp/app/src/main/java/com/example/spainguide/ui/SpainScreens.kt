package com.example.spainguide.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spainguide.data.LocalData
import com.example.spainguide.model.Category
import com.example.spainguide.model.CategoryAccent
import com.example.spainguide.model.Place

/**
 * Resuelve el color de "contenedor" (fondo) que le corresponde a una
 * categoría según el rol de Material 3 que tenga asignado.
 */
@Composable
private fun accentContainerColor(accent: CategoryAccent): Color = when (accent) {
    CategoryAccent.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.primaryContainer
    CategoryAccent.SECONDARY_CONTAINER -> MaterialTheme.colorScheme.secondaryContainer
    CategoryAccent.TERTIARY_CONTAINER -> MaterialTheme.colorScheme.tertiaryContainer
    CategoryAccent.PRIMARY_SOLID -> MaterialTheme.colorScheme.primary
}

/**
 * Resuelve el color de contenido (texto/ícono) que corresponde usar
 * sobre el color de contenedor devuelto por [accentContainerColor].
 */
@Composable
private fun accentContentColor(accent: CategoryAccent): Color = when (accent) {
    CategoryAccent.PRIMARY_CONTAINER -> MaterialTheme.colorScheme.onPrimaryContainer
    CategoryAccent.SECONDARY_CONTAINER -> MaterialTheme.colorScheme.onSecondaryContainer
    CategoryAccent.TERTIARY_CONTAINER -> MaterialTheme.colorScheme.onTertiaryContainer
    CategoryAccent.PRIMARY_SOLID -> MaterialTheme.colorScheme.onPrimary
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected) {
        accentContainerColor(category.accent)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (selected) {
        accentContentColor(category.accent)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(category.nameRes),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListItem(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Image(
                painter = painterResource(place.imageRes),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(place.nameRes),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(place.descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}

/** Pequeño badge con el nombre y color de la categoría del lugar. */
@Composable
private fun CategoryBadge(category: Category, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = accentContainerColor(category.accent),
        contentColor = accentContentColor(category.accent),
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(category.nameRes),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PlaceDetailScreen(
    place: Place,
    onBackPressed: () -> Unit,
    isFullScreen: Boolean = false,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackPressed()
    }
    val scrollState = rememberScrollState()
    val category = LocalData.categories.first { it.id == place.categoryId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        Image(
            painter = painterResource(place.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            CategoryBadge(category = category)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(place.nameRes),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(place.descriptionRes),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}