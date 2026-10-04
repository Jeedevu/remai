package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FocusModeScreen(
    onExit: () -> Unit
) {
    var totalSeconds by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(true) }
    var isCelebrationVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, totalSeconds) {
        if (isRunning && totalSeconds > 0) {
            delay(1000)
            totalSeconds--
        } else if (totalSeconds == 0) {
            isCelebrationVisible = true
        }
    }

    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val timeFormatted = "%02d:%02d".format(minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp))
                    .clickable { onExit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit", tint = RemOnSurface)
            }

            Box(
                modifier = Modifier
                    .background(RemSecondaryContainer, RoundedCornerShape(8.dp))
                    .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                    Text("FOCUS SPRINT ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                    .border(2.dp, RemInkBlack, RoundedCornerShape(10.dp))
                    .clickable {
                        totalSeconds = 25 * 60
                        isRunning = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = RemOnSurface)
            }
        }

        // Timer Dial Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HardShadowCard(
                modifier = Modifier.size(260.dp),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 5.dp,
                shape = CircleShape,
                contentPadding = PaddingValues(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(RemPrimaryContainer, CircleShape)
                        .border(3.dp, RemInkBlack, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "PHYSICS SPRINT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RemOnSurface,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = timeFormatted,
                            fontSize = 54.sp,
                            fontWeight = FontWeight.Black,
                            color = RemInkBlack,
                            letterSpacing = (-1).sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = RemSecondary, modifier = Modifier.size(16.dp))
                            Text(
                                text = "HIGH FOCUS STATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RemOnSurface
                            )
                        }
                    }
                }
            }

            // Play / Pause Controls
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) RemSecondaryContainer else RemPrimaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.border(2.dp, RemInkBlack, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (isRunning) Color.White else RemInkBlack
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) "PAUSE SPRINT" else "RESUME",
                        fontWeight = FontWeight.Black,
                        color = if (isRunning) Color.White else RemInkBlack
                    )
                }
            }
        }

        // Active Task Context Card
        HardShadowCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = RemSurfaceContainerLowest,
            shadowOffset = 3.dp,
            contentPadding = PaddingValues(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Physics Problem Set (Mod 4)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface
                    )
                    Box(
                        modifier = Modifier
                            .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Due Tomorrow 8PM", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Complete questions 1–10 from mechanics problem set. Don't forget free-body diagrams for harmonic oscillators.",
                    fontSize = 12.sp,
                    color = RemOnSurfaceVariant,
                    lineHeight = 16.sp
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.ScreenshotMonitor, contentDescription = null, tint = RemPrimary, modifier = Modifier.size(14.dp))
                    Text("Source: Syllabus screenshot parsed at 98% confidence", fontSize = 10.sp, color = RemOnSurfaceVariant)
                }
            }
        }

        // Complete Action Button
        StompButton(
            onClick = { isCelebrationVisible = true },
            backgroundColor = RemPrimaryContainer
        ) {
            Text("FINISH SPRINT & CLAIM +120 XP", fontSize = 13.sp, fontWeight = FontWeight.Black)
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = RemInkBlack)
        }
    }

    // Celebration Dialog
    if (isCelebrationVisible) {
        AlertDialog(
            onDismissRequest = { isCelebrationVisible = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RemMascotGraphic(size = 32.dp)
                    Text("SPRINT COMPLETED! 🎉", fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Boom! You knocked out Physics Module 4 without distractions.",
                        fontSize = 13.sp,
                        color = RemOnSurface
                    )
                    Box(
                        modifier = Modifier
                            .background(RemPrimaryContainer, RoundedCornerShape(6.dp))
                            .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "+120 XP EARNED • 7-DAY ZERO FORGETTING STREAK EXTENDED!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = RemOnSurface
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isCelebrationVisible = false
                        onExit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RemInkBlack)
                ) {
                    Text("BACK TO DASHBOARD", fontWeight = FontWeight.Black, color = RemPrimaryContainer)
                }
            }
        )
    }
}
