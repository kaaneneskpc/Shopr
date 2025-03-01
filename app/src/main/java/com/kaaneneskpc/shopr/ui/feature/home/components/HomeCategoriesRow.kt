package com.kaaneneskpc.shopr.ui.feature.home.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCategoriesRow(
    categories: List<String>,
    title: String,
    modifier: Modifier = Modifier,
    onCategorySelected: (String?) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            TextButton(onClick = { /* View all categories */ }) {
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories, key = { it }) { category ->
                val isSelected = selectedCategory == category
                
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newSelection = if (isSelected) null else category
                        selectedCategory = newSelection
                        onCategorySelected(newSelection)
                    },
                    label = {
                        Text(
                            text = category.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}