package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.TaskEntity
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    repository: RemRepository,
    onNavigateToQuickCapture: () -> Unit,
    onNavigateToFocusMode: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAssistant: () -> Unit
) {
    val tasks by repository.getAllTasks().collectAsState(initial = emptyList())
    val completedCount by repository.getCompletedCount().collectAsState(initial = 0)
    val totalCount by repository.getTotalCount().collectAsState(initial = 5)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface),
        containerColor = RemSurface,
        topBar = {
            HomeTopBar(
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Greeting Banner
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "GOOD MORNING, ALEX",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "👋", fontSize = 20.sp)
                        }
                        Text(
                            text = "Here's what you need to remember.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = RemOnSurfaceVariant
                        )
                    }

                    // Mini Brutalist Halftone Graphic
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(RemSurfaceContainer, RoundedCornerShape(8.dp))
                            .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        HalftoneDotMatrix(rows = 3, cols = 3, dotSize = 3.5.dp)
                    }
                }
            }

            // Hero Brutalist Urgent Alert Card
            item {
                HeroUrgentAttentionCard(
                    onTaskClick = { onNavigateToFocusMode() }
                )
            }

            // Today's Flow Section
            item {
                TodaysFlowHeader(
                    completed = completedCount,
                    total = if (totalCount == 0) 5 else totalCount,
                    onScheduleClick = onNavigateToCalendar
                )
            }

            // Flow Task Checklist items
            items(tasks.take(4), key = { it.id }) { task ->
                FlowTaskItemCard(
                    task = task,
                    onToggleComplete = {
                        coroutineScope.launch {
                            repository.toggleTaskComplete(task.id, !task.isCompleted)
                        }
                    },
                    onClick = onNavigateToFocusMode
                )
            }

            // Neural Radar Gap Analysis Box
            item {
                NeuralRadarCard(
                    onAutoBlockFocus = onNavigateToFocusMode
                )
            }

            // Zero Forgetting Streak Banner
            item {
                StreakBannerCard(streakDays = 7)
            }
        }
    }
}

@Composable
private fun HomeTopBar(
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
                    text = "HOME",
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
            // Notifications Bell
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp))
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = RemOnSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(RemPrimary, CircleShape)
                    .border(1.5.dp, RemInkBlack, CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = RemOnPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun HeroUrgentAttentionCard(
    onTaskClick: () -> Unit
) {
    HardShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = RemPrimaryContainer,
        shadowOffset = 4.dp,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Badges & Level Ribbon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(RemSecondary, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "URGENT ATTENTION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Row(
                    modifier = Modifier
                        .background(RemSurfaceContainerLowest, CircleShape)
                        .border(1.5.dp, RemInkBlack, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🧠⚡", fontSize = 12.sp)
                    Text(
                        text = "PANIC BOT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RemOnSurface
                    )
                }
            }

            // Headline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "3 THINGS",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface,
                        lineHeight = 32.sp
                    )
                    Text(
                        text = "NEED YOU",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = RemSecondary,
                        lineHeight = 32.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DEADLINE CRUNCH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "48H LEFT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface
                    )
                }
            }

            // Mascot Illustration Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                // Vector Graphic representation of energetic brain mascot
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RemMascotGraphic(size = 54.dp)
                    Column {
                        Text(
                            text = "SYLLABUS DEADLINE ALERT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = RemInkBlack
                        )
                        Text(
                            text = "Physics • English • Maths calc sync",
                            fontSize = 11.sp,
                            color = RemOnSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(RemInkBlack, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "⚡ ACTION REQUIRED TODAY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemPrimaryContainer
                    )
                }
            }

            // 3 Quick Action Task Items
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Task 1: Physics
                UrgentTaskCard(
                    icon = "📚",
                    iconBg = RemSecondaryFixed,
                    title = "Physics Assignment",
                    subtitle = "Due Tomorrow, 8:00 PM • Mod 4 (Q1-10)",
                    tag = "HIGH PRIORITY",
                    tagBg = RemSecondaryContainer,
                    tagColor = Color.White,
                    onClick = onTaskClick
                )

                // Task 2: English
                UrgentTaskCard(
                    icon = "📝",
                    iconBg = RemTertiaryFixed,
                    title = "English Essay Draft",
                    subtitle = "Due Wed, 11:59 PM • Lit Review",
                    tag = "MED PRIORITY",
                    tagBg = RemTertiaryContainer,
                    tagColor = RemOnTertiaryContainer,
                    onClick = onTaskClick
                )

                // Task 3: Maths
                UrgentTaskCard(
                    icon = "📅",
                    iconBg = RemErrorContainer,
                    title = "Maths Midterm Exam",
                    subtitle = "Due Friday, 9:00 AM • Calc Ch. 4-6",
                    tag = "CRITICAL",
                    tagBg = RemError,
                    tagColor = Color.White,
                    onClick = onTaskClick
                )
            }
        }
    }
}

@Composable
private fun UrgentTaskCard(
    icon: String,
    iconBg: Color,
    title: String,
    subtitle: String,
    tag: String,
    tagBg: Color,
    tagColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
            .border(2.dp, RemInkBlack, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
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
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(iconBg, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 14.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = RemOnSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(tagBg, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = tag,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = tagColor
                )
            }
        }
    }
}

@Composable
private fun TodaysFlowHeader(
    completed: Int,
    total: Int,
    onScheduleClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "TODAY'S FLOW",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface
                )

                Row(
                    modifier = Modifier
                        .background(RemInkBlack, RoundedCornerShape(4.dp))
                        .clickable { onScheduleClick() }
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SCHEDULE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Text(
                text = "$completed OF $total DONE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = RemOnSurface
            )
        }

        // Chunky Segmented Hazard Strip Progress Bar
        HardShadowCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = RemSurfaceContainer,
            shadowOffset = 2.dp,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                HazardSegmentedProgressBar(
                    completedSegments = completed,
                    totalSegments = total
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val percent = if (total > 0) (completed.toFloat() / total * 100).toInt() else 40
                    Text(
                        text = "$percent% VELOCITY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurfaceVariant
                    )
                    Text(
                        text = "+120 XP ON COMPLETE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RemSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun FlowTaskItemCard(
    task: TaskEntity,
    onToggleComplete: () -> Unit,
    onClick: () -> Unit
) {
    HardShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = RemSurfaceContainerLowest,
        shadowOffset = 2.dp,
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Checkbox button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(
                            if (task.isCompleted) RemPrimaryContainer else RemSurfaceContainerHigh,
                            RoundedCornerShape(6.dp)
                        )
                        .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                        .clickable { onToggleComplete() },
                    contentAlignment = Alignment.Center
                ) {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = RemInkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurface,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(
                        text = task.subtitle.ifBlank { task.dueDisplay },
                        fontSize = 11.sp,
                        color = RemOnSurfaceVariant
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (task.isNext && !task.isCompleted) {
                    Box(
                        modifier = Modifier
                            .background(RemPrimaryFixed, RoundedCornerShape(4.dp))
                            .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "NEXT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RemOnPrimaryFixed
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(RemSurfaceContainerHigh, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "⏱️ ${task.estimatedMinutes}M",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun NeuralRadarCard(
    onAutoBlockFocus: () -> Unit
) {
    HardShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = RemSurfaceContainerLow,
        shadowOffset = 2.5.dp,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("💡", fontSize = 16.sp)
                    Text(
                        text = "NEURAL RADAR",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                        .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "FREE TIME DETECTED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RemOnSurface
                    )
                }
            }

            // Description
            Text(
                text = "You have an open 2-hour gap between 2:00 PM and 4:00 PM. This is your highest focus state—perfect window to knock out Physics.",
                fontSize = 13.sp,
                color = RemOnSurface,
                lineHeight = 18.sp
            )

            // Timeline Visualizer Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Lecture slot
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
                        .border(1.dp, RemInkBlack.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1:00 PM", fontSize = 9.sp, color = RemOnSurfaceVariant, fontWeight = FontWeight.Bold)
                        Text("Lecture", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                    }
                }

                // Physics sprint slot (Highlighted)
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .background(RemSecondaryContainer, RoundedCornerShape(8.dp))
                        .border(2.dp, RemInkBlack, RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("2:00 - 4:00 PM", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
                        Text("PHYSICS SPRINT", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }

                // Group sync slot
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
                        .border(1.dp, RemInkBlack.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("4:30 PM", fontSize = 9.sp, color = RemOnSurfaceVariant, fontWeight = FontWeight.Bold)
                        Text("Group Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                    }
                }
            }

            // Auto-Block Button
            StompButton(
                onClick = onAutoBlockFocus,
                backgroundColor = RemInkBlack,
                contentColor = RemPrimaryContainer,
                borderColor = RemInkBlack,
                shadowOffset = 2.dp
            ) {
                Text(
                    text = "AUTO-BLOCK FOCUS WINDOW",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = RemPrimaryContainer
                )
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = RemPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun StreakBannerCard(streakDays: Int) {
    HardShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = RemSurfaceContainerLowest,
        shadowOffset = 2.dp,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(RemSecondary, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = "Zero Forgetting Streak",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface
                )
                Text(
                    text = "$streakDays consecutive days synced without missed syllabus deadlines.",
                    fontSize = 11.sp,
                    color = RemOnSurfaceVariant
                )
            }
        }
    }
}
