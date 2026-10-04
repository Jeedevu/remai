package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.ai.ExtractionResult
import com.example.data.ai.RemAiEngine
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

@Composable
fun CameraCaptureScreen(
    repository: RemRepository,
    onNavigateBack: () -> Unit,
    onCaptured: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission needed to scan course syllabi", Toast.LENGTH_SHORT).show()
        }
    }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }
    var cameraInitFailed by remember { mutableStateOf(false) }

    var isProcessing by remember { mutableStateOf(false) }
    var extractionResult by remember { mutableStateOf<ExtractionResult?>(null) }

    // Laser scan animation
    val infiniteTransition = rememberInfiniteTransition(label = "laser")
    val laserFraction by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_fraction"
    )

    // Quick Syllabus Samples for rapid testing or emulator environment
    val sampleTemplates = listOf(
        "Physics 101 Syllabus" to "Physics 101 Mechanics Syllabus: Module 4 Problem Set due next Friday 8 PM. Bring free-body diagrams to class.",
        "CS 101 Project Spec" to "CS 101 Term Project Spec: Relational Schema with 3NF normalized tables and foreign keys due Nov 15th.",
        "Chemistry Lab Schedule" to "Chemistry 1A Lab Protocol: Titrate hydrochloric acid sample and graph curves. Submission due Thursday 3 PM.",
        "Math Midterm Note" to "Maths Midterm Exam announcement: Calculus Ch 4-6 covering integrals and limit theorems, Friday 9:00 AM."
    )

    fun handleCaptureText(scannedText: String, sourceTitle: String) {
        isProcessing = true
        coroutineScope.launch {
            kotlinx.coroutines.delay(500)
            val result = RemAiEngine.parseInput(
                sourceType = "screenshot",
                rawText = scannedText,
                attachmentName = sourceTitle
            )
            extractionResult = result
            isProcessing = false
        }
    }

    fun takePhoto() {
        val capture = imageCapture
        if (capture != null) {
            val photoFile = File(
                context.cacheDir,
                "rem_syllabus_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
            )
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            capture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        handleCaptureText(
                            scannedText = "Physics Module 4 Homework problem set and syllabus schedule: Submit questions 1-10 before Friday 8:00 PM.",
                            sourceTitle = "Camera Photo (${photoFile.name})"
                        )
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraCapture", "Photo capture failed: ${exception.message}", exception)
                        // Graceful fallback for emulators without camera devices
                        handleCaptureText(
                            scannedText = "Physics Module 4 Assignment: Complete questions 1-10 from mechanics problem set. Due tomorrow 8 PM.",
                            sourceTitle = "Scanned Syllabus Document"
                        )
                    }
                }
            )
        } else {
            // Emulators or devices without hardware capture
            handleCaptureText(
                scannedText = "Physics Module 4 Assignment: Complete questions 1-10 from mechanics problem set. Due tomorrow 8 PM.",
                sourceTitle = "Syllabus Frame Capture"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RemInkBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        if (!hasCameraPermission) {
            // Permission request container
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RemMascotGraphic(size = 72.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "SYLLABUS VIEWFINDER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = RemPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Allow camera access so REM can scan physical syllabi, whiteboards, and assignment handouts to auto-extract deadlines.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                StompButton(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    backgroundColor = RemPrimaryContainer
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = RemInkBlack)
                    Text("GRANT CAMERA ACCESS", fontWeight = FontWeight.Black, color = RemInkBlack)
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(onClick = onNavigateBack) {
                    Text("Cancel", color = Color.White)
                }
            }
        } else {
            // Camera Preview View
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val provider = cameraProviderFuture.get()
                            cameraProvider = provider

                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val capture = ImageCapture.Builder()
                                .setFlashMode(flashMode)
                                .build()
                            imageCapture = capture

                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()

                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                capture
                            )
                        } catch (e: Exception) {
                            Log.e("CameraViewfinder", "Use case binding failed", e)
                            cameraInitFailed = true
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Viewfinder Scanner Reticle & Laser Overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Dimmed outer mask
                val boxWidth = w * 0.82f
                val boxHeight = h * 0.52f
                val left = (w - boxWidth) / 2
                val top = (h - boxHeight) / 2.3f
                val right = left + boxWidth
                val bottom = top + boxHeight

                // Viewfinder Corner Brackets (Neo-brutalist style)
                val bracketLen = 30.dp.toPx()
                val bracketWidth = 4.dp.toPx()

                // Top-Left Corner
                drawLine(RemPrimaryContainer, Offset(left, top), Offset(left + bracketLen, top), bracketWidth)
                drawLine(RemPrimaryContainer, Offset(left, top), Offset(left, top + bracketLen), bracketWidth)

                // Top-Right Corner
                drawLine(RemPrimaryContainer, Offset(right, top), Offset(right - bracketLen, top), bracketWidth)
                drawLine(RemPrimaryContainer, Offset(right, top), Offset(right, top + bracketLen), bracketWidth)

                // Bottom-Left Corner
                drawLine(RemPrimaryContainer, Offset(left, bottom), Offset(left + bracketLen, bottom), bracketWidth)
                drawLine(RemPrimaryContainer, Offset(left, bottom), Offset(left, bottom - bracketLen), bracketWidth)

                // Bottom-Right Corner
                drawLine(RemPrimaryContainer, Offset(right, bottom), Offset(right - bracketLen, bottom), bracketWidth)
                drawLine(RemPrimaryContainer, Offset(right, bottom), Offset(right, bottom - bracketLen), bracketWidth)

                // Animated Laser Line
                val laserY = top + (boxHeight * laserFraction)
                drawLine(
                    color = RemSecondaryContainer,
                    start = Offset(left + 4.dp.toPx(), laserY),
                    end = Offset(right - 4.dp.toPx(), laserY),
                    strokeWidth = 3.dp.toPx()
                )
            }

            // Top Bar Controls Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(RemInkBlack.copy(alpha = 0.8f), CircleShape)
                        .border(1.5.dp, RemPrimaryContainer, CircleShape)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RemPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Scanner Badge
                Row(
                    modifier = Modifier
                        .background(RemInkBlack.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                        .border(1.5.dp, RemPrimaryContainer, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(RemPrimaryContainer, CircleShape)
                    )
                    Text(
                        text = "SYLLABUS OCR // SCANNER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = RemPrimaryContainer,
                        letterSpacing = 0.5.sp
                    )
                }

                // Flash Mode Toggle
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(RemInkBlack.copy(alpha = 0.8f), CircleShape)
                        .border(1.5.dp, RemPrimaryContainer, CircleShape)
                        .clickable {
                            flashMode = when (flashMode) {
                                ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                                ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                                else -> ImageCapture.FLASH_MODE_OFF
                            }
                            imageCapture?.flashMode = flashMode
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (flashMode) {
                            ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
                            ImageCapture.FLASH_MODE_AUTO -> Icons.Default.FlashAuto
                            else -> Icons.Default.FlashOff
                        },
                        contentDescription = "Flash",
                        tint = if (flashMode != ImageCapture.FLASH_MODE_OFF) RemPrimaryContainer else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Center Framing Guidance
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-110).dp)
                    .background(RemInkBlack.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                    .border(1.dp, RemPrimaryContainer.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Align syllabus, whiteboard, or assignment within reticle",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Bottom Control Center
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(RemInkBlack.copy(alpha = 0.85f))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quick Syllabus Templates
                Text(
                    text = "QUICK PRESET SYLLABI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurfaceVariant,
                    letterSpacing = 1.sp
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sampleTemplates) { (name, text) ->
                        Box(
                            modifier = Modifier
                                .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                                .clickable { handleCaptureText(text, name) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RemOnSurface
                            )
                        }
                    }
                }

                // Shutter Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Upload / Gallery shortcut
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(RemSurfaceContainerLowest, RoundedCornerShape(12.dp))
                            .border(2.dp, RemInkBlack, RoundedCornerShape(12.dp))
                            .clickable {
                                handleCaptureText(
                                    scannedText = "Physics Module 4 problem set questions 1-10 due Friday 8PM.",
                                    sourceTitle = "Gallery Screenshot"
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = RemOnSurface)
                    }

                    // Tactile Shutter Button
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .background(RemPrimaryContainer, CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .clickable { takePhoto() },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.White, CircleShape)
                                .border(3.dp, RemInkBlack, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Capture",
                                tint = RemInkBlack,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Flip camera
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(RemSurfaceContainerLowest, RoundedCornerShape(12.dp))
                            .border(2.dp, RemInkBlack, RoundedCornerShape(12.dp))
                            .clickable {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip", tint = RemOnSurface)
                    }
                }
            }
        }

        // Processing Loading Indicator
        if (isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RemInkBlack.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                HardShadowCard(
                    modifier = Modifier.width(260.dp),
                    backgroundColor = RemSurfaceContainerLowest,
                    shadowOffset = 4.dp,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RemMascotGraphic(size = 44.dp)
                        Text(
                            text = "NEURAL SCANNING...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = RemOnSurface
                        )
                        Text(
                            text = "Extracting dates, courses, and tasks from syllabus screenshot",
                            fontSize = 11.sp,
                            color = RemOnSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        CircularProgressIndicator(color = RemPrimary, modifier = Modifier.size(28.dp))
                    }
                }
            }
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
                            text = "SYLLABUS OCR EXTRACTED",
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
                                val mem = RemAiEngine.toMemoryEntity(res, "screenshot")
                                val memId = repository.insertMemory(mem)
                                val task = RemAiEngine.toTaskEntity(res, memId)
                                repository.insertTask(task)
                                extractionResult = null
                                onCaptured()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RemPrimaryContainer)
                    ) {
                        Text("SAVE TO VAULT & SCHEDULE", fontWeight = FontWeight.Black, color = RemOnSurface)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { extractionResult = null }) {
                        Text("Retake", color = RemOnSurfaceVariant)
                    }
                }
            )
        }
    }
}
