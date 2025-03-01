package com.kaaneneskpc.shopr.ui.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    value: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }

    SearchBar(
        query = value,
        onQueryChange = onTextChanged,
        onSearch = { isActive = false },
        active = isActive,
        onActiveChange = { isActive = it },
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onTextChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    ) {
        // Search suggestions can be added here
    }
}
