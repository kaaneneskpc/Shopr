package com.kaaneneskpc.shopr.ui.feature.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kaaneneskpc.domain.model.UserProfile

@Composable
fun ProfileHeader(
    userProfile: UserProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                ) {
                    if (userProfile.profileImage != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(userProfile.profileImage)
                                    .build()
                            ),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 3.dp,
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 3.dp,
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(16.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    FloatingActionButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userProfile.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userProfile.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        tonalElevation = 1.dp
                    ) {
                        Text(
                            text = "Üyelik: ${
                                userProfile.createdAt.split(" ").firstOrNull() ?: ""
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
} 