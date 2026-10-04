package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.MemoryEntity
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MemoryVaultScreen(
    repository: RemRepository,
    onNavigateToQuickCapture: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    val coroutineScope = rememberCoroutineScope()

    val memories by repository.getAllMemories().collectAsState(initial = emptyList())

    val filteredMemories = remember(memories, searchQuery, selectedCategory) {
        memories.filter { mem ->
            val matchesCategory = (selectedCategory == "all") || (mem.category.equals(selectedCategory, ignoreCase = true))
            val matchesSearch = searchQuery.isBlank() ||
                    mem.title.contains(searchQuery, ignoreCase = true) ||
                    mem.summary.contains(searchQuery, ignoreCase = true) ||
                    mem.course.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface),
        containerColor = RemSurface,
        topBar = {
            VaultTopBar(
                onProfileClick = onNavigateToProfile,
                onNotificationClick = onNavigateToAssistant
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Intro Search Banner Card
            item {
                HardShadowCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = RemSurfaceContainerLowest,
                    shadowOffset = 3.dp,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "YOUR MEMORY VAULT",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface
                            )

                            Row(
                                modifier = Modifier
                                    .background(RemPrimaryContainer, RoundedCornerShape(6.dp))
                                    .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(13.dp))
                                Text(
                                    text = "${memories.size} ITEMS STORED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RemOnSurface
                                )
                            }
                        }

                        Text(
                            text = "AI automatically tags, organizes, and tracks deadlines from your chaos.",
                            fontSize = 12.sp,
                            color = RemOnSurfaceVariant
                        )

                        // Search Input
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Search notes, syllabi, screenshots...", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RemOnSurface) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = RemOnSurface)
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = RemSurfaceContainerLow,
                                unfocusedContainerColor = RemSurfaceContainerLow,
                                focusedBorderColor = RemInkBlack,
                                unfocusedBorderColor = RemInkBlack.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf(
                        "all" to "ALL (${memories.size})",
                        "assignments" to "ASSIGNMENTS (18)",
                        "exams" to "EXAMS (4)",
                        "projects" to "PROJECTS (7)",
                        "notes" to "NOTES (13)"
                    )

                    categories.forEach { (catId, label) ->
                        val isSelected = selectedCategory == catId
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) RemInkBlack else when (catId) {
                                        "assignments" -> RemPrimaryContainer
                                        "exams" -> RemSecondaryContainer
                                        "projects" -> RemTertiary
                                        else -> RemSurfaceContainerLowest
                                    },
                                    RoundedCornerShape(8.dp)
                                )
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                                .clickable { selectedCategory = catId }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) RemOnPrimary else when (catId) {
                                    "assignments" -> RemOnSurface
                                    "exams" -> Color.White
                                    "projects" -> Color.White
                                    else -> RemOnSurface
                                }
                            )
                        }
                    }
                }
            }

            // Memory List Stack
            if (filteredMemories.isEmpty()) {
                item {
                    HardShadowCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = RemSurfaceContainerLowest,
                        contentPadding = PaddingValues(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RemMascotGraphic(size = 48.dp)
                            Text(
                                text = "Nothing forgotten yet.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RemOnSurface
                            )
                            Text(
                                text = "Send screenshots, syllabi or voice notes to start remembering.",
                                fontSize = 12.sp,
                                color = RemOnSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            StompButton(
                                onClick = onNavigateToQuickCapture,
                                backgroundColor = RemPrimaryContainer
                            ) {
                                Text("CAPTURE NEW MEMORY", fontSize = 12.sp, fontWeight = FontWeight.Black)
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                    }
                }
            } else {
                items(filteredMemories, key = { it.id }) { memory ->
                    MemoryCardItem(
                        memory = memory,
                        onToggleStar = {
                            coroutineScope.launch {
                                repository.toggleStar(memory.id, !memory.isStarred)
                            }
                        }
                    )
                }
            }

            // Mascot Status Footer
            item {
                HardShadowCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = RemSurfaceContainerLow,
                    shadowOffset = 2.5.dp,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RemMascotGraphic(size = 48.dp)
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(RemSecondaryContainer, CircleShape)
                                )
                                Text(
                                    text = "AUTO-SYNC LIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RemOnSurface,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = "All memories synced",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface
                            )
                            Text(
                                text = "42 entries indexed and ready for exam prep.",
                                fontSize = 11.sp,
                                color = RemOnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VaultTopBar(
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RemMascotGraphic(size = 36.dp)
            Column {
                Text(
                    text = "REM",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "MEMORY VAULT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemOnSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp))
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = RemOnSurface)
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(RemPrimary, CircleShape)
                    .border(1.5.dp, RemInkBlack, CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = RemOnPrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun MemoryCardItem(
    memory: MemoryEntity,
    onToggleStar: () -> Unit
) {
    HardShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = RemSurfaceContainerLowest,
        shadowOffset = 3.dp,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Header: Icon + Title + Due Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val (icon, bg) = when (memory.category) {
                        "projects" -> Icons.Default.Terminal to RemTertiaryContainer
                        "exams" -> Icons.Default.Science to RemSecondaryFixed
                        "notes" -> Icons.Default.MenuBook to RemSurfaceContainerHighest
                        else -> Icons.Default.Description to RemPrimaryFixed
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(bg, RoundedCornerShape(8.dp))
                            .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(18.dp))
                    }

                    Text(
                        text = memory.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface,
                        maxLines = 1
                    )
                }

                if (memory.deadline.isNotBlank()) {
                    val badgeBg = when {
                        memory.deadline.contains("Oct", true) || memory.deadline.contains("Nov", true) -> RemPrimaryContainer
                        memory.deadline.contains("Thursday", true) -> RemSecondaryFixedDim
                        else -> RemSurfaceContainerHigh
                    }
                    Box(
                        modifier = Modifier
                            .background(badgeBg, RoundedCornerShape(4.dp))
                            .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = memory.deadline,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RemOnSurface
                        )
                    }
                }
            }

            // Summary
            Text(
                text = memory.summary,
                fontSize = 12.sp,
                color = RemOnSurface,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 40.dp)
            )

            // Source Detail & Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(RemSurfaceContainer, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val icon = when (memory.sourceType) {
                        "pdf" -> Icons.Default.PictureAsPdf
                        "voice" -> Icons.Default.Mic
                        "link" -> Icons.Default.Link
                        else -> Icons.Default.PhotoCamera
                    }
                    Icon(icon, contentDescription = null, tint = RemOnSurfaceVariant, modifier = Modifier.size(13.dp))
                    Text(
                        text = memory.sourceDetail.ifBlank { "Synced memory" },
                        fontSize = 10.sp,
                        color = RemOnSurfaceVariant,
                        maxLines = 1
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Star button
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                if (memory.isStarred) RemPrimaryContainer else RemSurfaceContainerLow,
                                RoundedCornerShape(6.dp)
                            )
                            .border(1.dp, RemInkBlack, RoundedCornerShape(6.dp))
                            .clickable { onToggleStar() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = if (memory.isStarred) RemSecondary else RemOnSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Share button
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(RemSurfaceContainerLow, RoundedCornerShape(6.dp))
                            .border(1.dp, RemInkBlack, RoundedCornerShape(6.dp))
                            .clickable { /* Share Intent */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = RemOnSurface,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
