package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*

data class CalendarEventItem(
    val time: String,
    val title: String,
    val subtitle: String,
    val type: String, // "focus", "class", "deadline", "sync"
    val isHighlighted: Boolean = false
)

@Composable
fun CalendarScreen(
    repository: RemRepository,
    onNavigateBack: () -> Unit,
    onStartFocusSprint: () -> Unit
) {
    var selectedDay by remember { mutableIntStateOf(5) } // Day 5 is Wednesday

    val daysOfWeek = listOf(
        "MON" to 3,
        "TUE" to 4,
        "WED" to 5,
        "THU" to 6,
        "FRI" to 7,
        "SAT" to 8,
        "SUN" to 9
    )

    val scheduleEvents = remember(selectedDay) {
        when (selectedDay) {
            5 -> listOf(
                CalendarEventItem("10:00 AM", "Review Bio Notes", "Cellular Respiration (Completed)", "class"),
                CalendarEventItem("1:00 PM", "Macroeconomics Lecture", "Hall B • Elasticity & Demand", "class"),
                CalendarEventItem("2:00 - 4:00 PM", "Physics Sprint [NEURAL RADAR]", "Auto-blocked highest focus window", "focus", isHighlighted = true),
                CalendarEventItem("4:30 PM", "Group Sync: AI Ethics Lab", "Zoom Call • Project Scope", "sync"),
                CalendarEventItem("11:59 PM", "English Essay Draft Due", "Canvas Submission Portal", "deadline")
            )
            6 -> listOf(
                CalendarEventItem("11:00 AM", "Chemistry Lab Session", "Titration Protocol Experiment", "class"),
                CalendarEventItem("3:00 PM", "Prof. Miller Office Hours", "Room 302 • CS 101 Questions", "sync", isHighlighted = true),
                CalendarEventItem("8:00 PM", "Physics Module 4 Due", "WebAssign Portal (Q1-10)", "deadline")
            )
            7 -> listOf(
                CalendarEventItem("9:00 AM", "Maths Midterm Exam", "Calculus Ch. 4-6 (CRITICAL)", "deadline", isHighlighted = true),
                CalendarEventItem("2:00 PM", "CS 101 Lab Review", "Relational Schema 3NF", "class")
            )
            else -> listOf(
                CalendarEventItem("10:00 AM", "Independent Study Session", "Library Room 4", "focus"),
                CalendarEventItem("4:00 PM", "Weekly Planning & Sprint Sync", "REM AI Memory Organizer", "sync")
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface),
        containerColor = RemSurface,
        topBar = {
            CalendarTopBar(onBackClick = onNavigateBack)
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
            // Month Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "OCTOBER 2026",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = RemOnSurface,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Midterm & Submission Week",
                            fontSize = 12.sp,
                            color = RemOnSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                            .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("TODAY", fontSize = 11.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
                    }
                }
            }

            // Days Ribbon
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { (label, dayNum) ->
                        val isSelected = selectedDay == dayNum
                        Column(
                            modifier = Modifier
                                .width(42.dp)
                                .background(
                                    if (isSelected) RemPrimaryContainer else RemSurfaceContainerLowest,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) RemInkBlack else RemInkBlack.copy(alpha = 0.15f),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedDay = dayNum }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) RemOnSurface else RemOnSurfaceVariant
                            )
                            Text(
                                text = "$dayNum",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface
                            )
                            if (dayNum == 5 || dayNum == 7) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(RemSecondaryContainer, CircleShape)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            // Neural Sprint Highlight Banner
            item {
                HardShadowCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = RemSecondaryFixed,
                    shadowOffset = 2.5.dp,
                    contentPadding = PaddingValues(12.dp)
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
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = RemSecondary)
                            Column {
                                Text(
                                    text = "2-HOUR FOCUS GAP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RemSecondary
                                )
                                Text(
                                    text = "Physics Sprint: 2:00 - 4:00 PM",
                                    fontSize = 11.sp,
                                    color = RemOnSecondaryFixed
                                )
                            }
                        }

                        Button(
                            onClick = onStartFocusSprint,
                            colors = ButtonDefaults.buttonColors(containerColor = RemSecondaryContainer),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("START SPRINT", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
            }

            // Timeline Header
            item {
                Text(
                    text = "TIMELINE SCHEDULE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface,
                    letterSpacing = 0.5.sp
                )
            }

            // Events List
            items(scheduleEvents) { event ->
                EventTimelineCard(event = event)
            }
        }
    }
}

@Composable
private fun EventTimelineCard(event: CalendarEventItem) {
    val (cardBg, borderCol) = if (event.isHighlighted) {
        RemSecondaryContainer to RemInkBlack
    } else {
        RemSurfaceContainerLowest to RemInkBlack.copy(alpha = 0.2f)
    }

    HardShadowCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = cardBg,
        borderColor = borderCol,
        shadowOffset = if (event.isHighlighted) 3.dp else 1.5.dp,
        contentPadding = PaddingValues(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.time,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (event.isHighlighted) Color.White.copy(alpha = 0.9f) else RemSecondary
                )
                Text(
                    text = event.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = if (event.isHighlighted) Color.White else RemOnSurface
                )
                Text(
                    text = event.subtitle,
                    fontSize = 11.sp,
                    color = if (event.isHighlighted) Color.White.copy(alpha = 0.85f) else RemOnSurfaceVariant
                )
            }

            val typeBadge = when (event.type) {
                "focus" -> "SPURT" to RemPrimaryContainer
                "deadline" -> "DUE" to RemErrorContainer
                "class" -> "CLASS" to RemTertiaryContainer
                else -> "SYNC" to RemSurfaceContainerHigh
            }

            Box(
                modifier = Modifier
                    .background(typeBadge.second, RoundedCornerShape(4.dp))
                    .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = typeBadge.first,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RemOnSurface
                )
            }
        }
    }
}

@Composable
private fun CalendarTopBar(onBackClick: () -> Unit) {
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RemOnSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            RemMascotGraphic(size = 32.dp)

            Text(
                text = "ACADEMIC RADAR",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = RemOnSurface,
                letterSpacing = (-0.5).sp
            )
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(18.dp))
        }
    }
}
