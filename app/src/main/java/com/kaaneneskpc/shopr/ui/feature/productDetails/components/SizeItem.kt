package com.kaaneneskpc.shopr.ui.feature.productDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kaaneneskpc.shopr.ui.theme.Blue

@Composable
fun SizeItem(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                if (isSelected) Blue else Color.Transparent
            )
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = size,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}