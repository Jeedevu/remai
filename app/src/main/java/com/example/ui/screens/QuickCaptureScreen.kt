package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.ExtractionResult
import com.example.data.ai.RemAiEngine
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun QuickCaptureScreen(
    repository: RemRepository,
    onNavigateBack: () -> Unit,
    onCaptureSaved: () -> Unit,
    onOpenScanner: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var showInputModal by remember { mutableStateOf<String?>(null) } // "screenshot", "pdf", "voice", "type", "photo", "link"
    var modalInputText by remember { mutableStateOf("") }
    var extractionResult by remember { mutableStateOf<ExtractionResult?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    fun processInput(type: String, text: String) {
        isProcessing = true
        coroutineScope.launch {
            kotlinx.coroutines.delay(400) // Brief simulated neural engine parse
            val result = RemAiEngine.parseInput(type, text)
            extractionResult = result
            isProcessing = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface),
        containerColor = RemSurface,
        topBar = {
            CaptureTopBar(onBackClick = onNavigateBack)
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
            // Live Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(RemInkBlack, CircleShape)
                    )
                    Text(
                        text = "LIVE INGESTION MODE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .background(RemSurfaceContainerHigh, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = RemPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "AUTO-SYNC ON",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurface
                    )
                }
            }

            // Headline Block
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                        .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = RemOnSurface, modifier = Modifier.size(14.dp))
                        Text(
                            text = "ZERO EFFORT INGESTION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemOnSurface
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "WHAT SHOULD I ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface
                    )
                    Box(
                        modifier = Modifier
                            .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "REMEMBER?",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = RemOnSurface
                        )
                    }
                }

                Text(
                    text = "Don't organize it. Just send it. AI figures out the deadlines, topics, & priority.",
                    fontSize = 13.sp,
                    color = RemOnSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            // 6 Grid Action Cards (2 Columns)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Screenshot (Popular)
                    CaptureTile(
                        modifier = Modifier.weight(1f),
                        badgeText = "🔥 POPULAR",
                        badgeBg = RemInkBlack,
                        badgeColor = RemPrimaryContainer,
                        icon = Icons.Default.ScreenshotMonitor,
                        iconBg = RemInkBlack,
                        iconColor = RemPrimaryContainer,
                        title = "SCREENSHOT",
                        desc = "Class chats, slides, Instagram stories",
                        actionText = "CAPTURE",
                        tileBg = RemPrimaryContainer,
                        onClick = onOpenScanner
                    )

                    // PDF / File (Auto-scan)
                    CaptureTile(
                        modifier = Modifier.weight(1f),
                        badgeText = "AUTO-SCAN",
                        badgeBg = RemInkBlack,
                        badgeColor = Color.White,
                        icon = Icons.Default.PictureAsPdf,
                        iconBg = RemSurfaceContainerLowest,
                        iconColor = RemSecondaryContainer,
                        title = "PDF / FILE",
                        desc = "Syllabi, worksheets, lecture slide decks",
                        actionText = "UPLOAD",
                        tileBg = RemSecondaryContainer,
                        titleColor = Color.White,
                        descColor = Color.White.copy(alpha = 0.9f),
                        actionColor = Color.White,
                        onClick = {
                            showInputModal = "pdf"
                            modalInputText = "CS 101 Course Syllabus - Term Project: Database Design with 3NF tables due Nov 15th."
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Voice Note (Instant)
                    CaptureTile(
                        modifier = Modifier.weight(1f),
                        badgeText = "INSTANT",
                        badgeBg = RemTertiary,
                        badgeColor = Color.White,
                        icon = Icons.Default.Mic,
                        iconBg = RemTertiary,
                        iconColor = Color.White,
                        title = "VOICE NOTE",
                        desc = "Rant your raw thoughts, AI builds tasks",
                        actionText = "RECORD",
                        tileBg = RemTertiaryContainer,
                        titleColor = RemOnTertiaryContainer,
                        descColor = RemOnTertiaryContainer.copy(alpha = 0.85f),
                        actionColor = RemOnTertiaryContainer,
                        onClick = {
                            showInputModal = "voice"
                            modalInputText = "Hey REM, Dr. Thorne just announced the Chemistry lab titration curves are due Thursday before 5 PM!"
                        }
                    )

                    // Quick Type
                    CaptureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.EditNote,
                        iconBg = RemSurfaceContainerHigh,
                        iconColor = RemOnSurface,
                        title = "QUICK TYPE",
                        desc = "Jot fragments or paste clipboard text",
                        actionText = "WRITE",
                        tileBg = RemSurfaceContainerLowest,
                        onClick = {
                            showInputModal = "type"
                            modalInputText = ""
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Take Photo
                    CaptureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.PhotoCamera,
                        iconBg = RemInkBlack,
                        iconColor = Color.White,
                        title = "TAKE PHOTO",
                        desc = "Whiteboards, handwritten notes & books",
                        actionText = "SNAP",
                        tileBg = RemSurfaceContainerHigh,
                        onClick = onOpenScanner
                    )

                    // Share Link
                    CaptureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Link,
                        iconBg = RemSecondary,
                        iconColor = Color.White,
                        title = "SHARE LINK",
                        desc = "Canvas portals, Docs, Notion or web pages",
                        actionText = "INSERT",
                        tileBg = RemSecondaryFixed,
                        titleColor = RemOnSecondaryFixed,
                        descColor = RemOnSecondaryFixed.copy(alpha = 0.85f),
                        actionColor = RemOnSecondaryFixed,
                        onClick = {
                            showInputModal = "link"
                            modalInputText = "https://canvas.berkeley.edu/courses/macroeconomics/ch5-reading"
                        }
                    )
                }
            }

            // Smart Paste Card
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 2.5.dp,
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(RemPrimary, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("SMART PASTE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Text(
                            text = "Found in clipboard: \"Prof Miller Office Hours Thurs 3pm\"",
                            fontSize = 11.sp,
                            color = RemOnSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RemSurfaceContainer, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\"Prof Miller Office Hours Thurs 3pm...\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RemOnSurface,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )

                        Box(
                            modifier = Modifier
                                .background(RemPrimaryContainer, RoundedCornerShape(6.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                                .clickable {
                                    processInput("text", "Prof Miller Office Hours Thurs 3pm in Room 302 for CS 101 questions.")
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "INGEST NOW",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = RemOnSurface
                            )
                        }
                    }
                }
            }

            // Pro Tip Banner
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemInkBlack,
                shadowOffset = 2.5.dp,
                contentPadding = PaddingValues(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(RemPrimaryContainer, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = RemInkBlack)
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("PRO TIP", fontSize = 12.sp, fontWeight = FontWeight.Black, color = RemPrimaryContainer)
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("FAST TRACK", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        Text(
                            text = "Share directly to REM from WhatsApp, Chrome, or Canvas using your native system Share Sheet.",
                            fontSize = 11.sp,
                            color = RemSurfaceContainerHighest,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }

    // Modal Sheet for typing / confirming text input
    if (showInputModal != null) {
        val type = showInputModal!!
        AlertDialog(
            onDismissRequest = { showInputModal = null },
            title = {
                Text(
                    text = "INGEST ${type.uppercase()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Paste text, syllabi, assignment details, or notes:",
                        fontSize = 12.sp,
                        color = RemOnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = modalInputText,
                        onValueChange = { modalInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        placeholder = { Text("e.g. Physics homework 5 due next Monday 11:59 PM...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RemInkBlack,
                            unfocusedBorderColor = RemInkBlack
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val input = modalInputText.ifBlank { "Quick Memory Capture" }
                        showInputModal = null
                        processInput(type, input)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RemPrimaryContainer)
                ) {
                    Text("AI ANALYZE & EXTRACT", fontWeight = FontWeight.Black, color = RemOnSurface)
                }
            },
            dismissButton = {
                TextButton(onClick = { showInputModal = null }) {
                    Text("Cancel", color = RemOnSurfaceVariant)
                }
            }
        )
    }

    // AI Extraction Confirmation Dialog
    if (extractionResult != null) {
        val res = extractionResult!!
        AlertDialog(
            onDismissRequest = { extractionResult = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RemMascotGraphic(size = 28.dp)
                    Text(
                        text = "AI EXTRACTION CONFIRMED",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(res.course, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RemPrimary)
                        Box(
                            modifier = Modifier
                                .background(RemSecondaryContainer, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(res.priority, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Text(res.title, fontSize = 15.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
                    Text(res.summary, fontSize = 12.sp, color = RemOnSurfaceVariant)

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("📅 ${res.deadline}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        Text("🎯 ${res.confidence}% Conf.", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemSecondary)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val mem = RemAiEngine.toMemoryEntity(res, "quick_capture")
                            val memId = repository.insertMemory(mem)
                            val task = RemAiEngine.toTaskEntity(res, memId)
                            repository.insertTask(task)
                            extractionResult = null
                            onCaptureSaved()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RemPrimaryContainer)
                ) {
                    Text("SAVE TO VAULT & SCHEDULE", fontWeight = FontWeight.Black, color = RemOnSurface)
                }
            },
            dismissButton = {
                TextButton(onClick = { extractionResult = null }) {
                    Text("Discard", color = RemOnSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun CaptureTopBar(onBackClick: () -> Unit) {
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
                text = "QUICK CAPTURE",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = RemOnSurface,
                letterSpacing = (-0.5).sp
            )
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .background(RemPrimary, CircleShape)
                .border(1.5.dp, RemInkBlack, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = RemOnPrimary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun CaptureTile(
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    badgeBg: Color = RemInkBlack,
    badgeColor: Color = Color.White,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    desc: String,
    actionText: String,
    tileBg: Color,
    titleColor: Color = RemOnSurface,
    descColor: Color = RemOnSurfaceVariant,
    actionColor: Color = RemOnSurface,
    onClick: () -> Unit
) {
    HardShadowCard(
        modifier = modifier,
        backgroundColor = tileBg,
        shadowOffset = 2.5.dp,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(12.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(badgeBg, RoundedCornerShape(bottomStart = 6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(badgeText, fontSize = 8.sp, fontWeight = FontWeight.Black, color = badgeColor)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(iconBg, RoundedCornerShape(8.dp))
                        .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = titleColor
                )

                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = descColor,
                    lineHeight = 14.sp,
                    minLines = 2,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(actionText, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = actionColor)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = actionColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
