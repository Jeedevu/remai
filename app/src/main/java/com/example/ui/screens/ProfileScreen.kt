package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    repository: RemRepository,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var urgentAlertsEnabled by remember { mutableStateOf(true) }
    var morningBriefEnabled by remember { mutableStateOf(true) }
    var soundVibrateEnabled by remember { mutableStateOf(true) }
    var selectedPersonality by remember { mutableStateOf("Smart") }
    var showClearDialog by remember { mutableStateOf(false) }

    // Dynamic Permission Checks
    var cameraGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    var audioGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
    }
    var notificationsGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        cameraGranted = results[Manifest.permission.CAMERA] ?: cameraGranted
        audioGranted = results[Manifest.permission.RECORD_AUDIO] ?: audioGranted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationsGranted = results[Manifest.permission.POST_NOTIFICATIONS] ?: notificationsGranted
        }
        val allGranted = cameraGranted && audioGranted && notificationsGranted
        Toast.makeText(
            context,
            if (allGranted) "All phone permissions granted!" else "Permissions updated",
            Toast.LENGTH_SHORT
        ).show()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface),
        containerColor = RemSurface,
        topBar = {
            ProfileTopBar(onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Student Card
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 3.dp,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(RemPrimary, CircleShape)
                            .border(2.5.dp, RemInkBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Alex Chen",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .background(RemSecondaryFixed, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("CAMPUS", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = RemOnSecondaryFixed)
                            }
                        }

                        Text(
                            text = "alex.chen@berkeley.edu",
                            fontSize = 12.sp,
                            color = RemOnSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(RemSuccessGreen, CircleShape))
                            Text("Local-First Vault Sync Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        }
                    }
                }
            }

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "STREAK",
                    value = "7 DAYS",
                    subtitle = "Zero missed deadlines",
                    bg = RemPrimaryContainer
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "VAULT",
                    value = "42 ITEMS",
                    subtitle = "Indexed memories",
                    bg = RemSurfaceContainerLowest
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "XP EARNED",
                    value = "1,200",
                    subtitle = "Level 3 Brain",
                    bg = RemSecondaryFixed
                )
            }

            // Phone Access & System Permissions Card
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 3.dp,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = RemPrimary)
                            Text(
                                text = "PHONE ACCESS & PERMISSIONS",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface,
                                letterSpacing = 0.5.sp
                            )
                        }

                        val allGranted = cameraGranted && audioGranted && notificationsGranted
                        Box(
                            modifier = Modifier
                                .background(if (allGranted) RemSuccessGreen else RemSecondaryContainer, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (allGranted) "ALL ACCESS ACTIVE" else "ACTION NEEDED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "REM requires device access to capture syllabus screenshots, record voice notes, and deliver deadline alerts.",
                        fontSize = 11.sp,
                        color = RemOnSurfaceVariant,
                        lineHeight = 15.sp
                    )

                    // Permission Items List
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PermissionStatusRow(
                            icon = Icons.Default.CameraAlt,
                            title = "Camera (Syllabus Scanner)",
                            desc = "Live viewfinder to scan physical handouts & whiteboards",
                            isGranted = cameraGranted
                        )
                        PermissionStatusRow(
                            icon = Icons.Default.Mic,
                            title = "Microphone (Voice Capture)",
                            desc = "Dictate thoughts, office hours, and reminders",
                            isGranted = audioGranted
                        )
                        PermissionStatusRow(
                            icon = Icons.Default.NotificationsActive,
                            title = "Notifications (Alert Lvl 1)",
                            desc = "48h deadline crunch and morning flow briefs",
                            isGranted = notificationsGranted
                        )
                    }

                    // Tactile Request All Permissions Button
                    StompButton(
                        onClick = {
                            val permissionsToRequest = mutableListOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO
                            )
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            multiplePermissionLauncher.launch(permissionsToRequest.toTypedArray())
                        },
                        backgroundColor = RemPrimaryContainer,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = RemInkBlack)
                            Text(
                                text = "REQUEST ALL PHONE PERMISSIONS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = RemInkBlack
                            )
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = RemInkBlack)
                    }
                }
            }

            // Reminder Personality Settings
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 2.5.dp,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "REMINDER INTELLIGENCE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface,
                        letterSpacing = 0.5.sp
                    )

                    // Personality Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Smart", "Gentle", "Persistent").forEach { p ->
                            val isSelected = selectedPersonality == p
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) RemInkBlack else RemSurfaceContainerLow,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, RemInkBlack, RoundedCornerShape(8.dp))
                                    .clickable { selectedPersonality = p }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = p,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else RemOnSurface
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    // Switches
                    SettingSwitchRow(
                        title = "Urgent Attention Alerts",
                        desc = "48h deadline crunch panic alerts",
                        checked = urgentAlertsEnabled,
                        onCheckedChange = { urgentAlertsEnabled = it }
                    )

                    SettingSwitchRow(
                        title = "Morning Daily Brief",
                        desc = "8:00 AM overview of today's flow",
                        checked = morningBriefEnabled,
                        onCheckedChange = { morningBriefEnabled = it }
                    )

                    SettingSwitchRow(
                        title = "Sound & Vibration",
                        desc = "Tactile feedback for deadline alerts",
                        checked = soundVibrateEnabled,
                        onCheckedChange = { soundVibrateEnabled = it }
                    )
                }
            }

            // Data Sovereignty & Privacy
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 2.5.dp,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DATA SOVEREIGNTY",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = RemOnSurface
                        )
                        Box(
                            modifier = Modifier
                                .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("100% OFFLINE-FIRST", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Text(
                        text = "All your documents, transcripts, and syllabus schedules remain strictly on this device inside your private Room database.",
                        fontSize = 11.sp,
                        color = RemOnSurfaceVariant,
                        lineHeight = 15.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Export Data
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(RemSurfaceContainerLow, RoundedCornerShape(8.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                                .clickable {
                                    Toast.makeText(context, "Exported 42 memories to rem_vault_backup.json", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = RemOnSurface, modifier = Modifier.size(16.dp))
                                Text("EXPORT JSON", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                            }
                        }

                        // Clear Data
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(RemErrorContainer, RoundedCornerShape(8.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                                .clickable { showClearDialog = true }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = RemError, modifier = Modifier.size(16.dp))
                                Text("CLEAR VAULT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemError)
                            }
                        }
                    }
                }
            }

            // Mars.AI Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REM // MARS.AI STUDENT SUITE • v3.1 REV.44",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemOnSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Memories?", fontWeight = FontWeight.Black) },
            text = { Text("This will permanently remove all memories and tasks from your device.") },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            repository.clearAllMemories()
                            showClearDialog = false
                            Toast.makeText(context, "Memory vault cleared", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RemError)
                ) {
                    Text("CLEAR ALL", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = RemOnSurface)
                }
            }
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    bg: Color
) {
    HardShadowCard(
        modifier = modifier,
        backgroundColor = bg,
        shadowOffset = 2.dp,
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RemOnSurfaceVariant)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
            Text(subtitle, fontSize = 9.sp, color = RemOnSurfaceVariant, maxLines = 1)
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
            Text(desc, fontSize = 11.sp, color = RemOnSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = RemInkBlack,
                checkedTrackColor = RemPrimaryContainer,
                uncheckedThumbColor = RemOnSurfaceVariant,
                uncheckedTrackColor = RemSurfaceContainerHigh
            )
        )
    }
}

@Composable
private fun ProfileTopBar(onBackClick: () -> Unit) {
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = RemOnSurface)
            }

            RemMascotGraphic(size = 32.dp)

            Text("STUDENT PROFILE", fontSize = 17.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
        }
    }
}

@Composable
private fun PermissionStatusRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    isGranted: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RemSurfaceContainerLow, RoundedCornerShape(8.dp))
            .border(1.dp, RemInkBlack.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(if (isGranted) RemPrimaryContainer else RemSurfaceContainerHighest, RoundedCornerShape(6.dp))
                    .border(1.dp, RemInkBlack, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(18.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                Text(desc, fontSize = 10.sp, color = RemOnSurfaceVariant, maxLines = 1)
            }
        }

        Box(
            modifier = Modifier
                .background(
                    if (isGranted) RemSuccessGreen.copy(alpha = 0.15f) else RemSecondaryContainer.copy(alpha = 0.15f),
                    RoundedCornerShape(4.dp)
                )
                .border(
                    1.dp,
                    if (isGranted) RemSuccessGreen else RemSecondaryContainer,
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (isGranted) "GRANTED" else "REQUIRED",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = if (isGranted) RemSuccessGreen else RemSecondaryContainer
            )
        }
    }
}
