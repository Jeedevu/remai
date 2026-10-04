package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun BootScreen(
    onBootDashboard: () -> Unit,
    onSkipToOnboarding: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Smooth progress animation from 0% up to 84%
    val progressAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progressAnim.animateTo(
            targetValue = 0.84f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Utilities & Mars.AI Sub-Branding Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Planetary sticker badge
                Row(
                    modifier = Modifier
                        .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
                        .border(2.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(RemSecondaryContainer, CircleShape)
                            .border(1.dp, RemInkBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    Text(
                        text = "CREATED BY MARS.AI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemInkBlack,
                        letterSpacing = 0.5.sp
                    )
                }

                // Telemetry status tag
                Row(
                    modifier = Modifier
                        .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                        .border(2.dp, RemInkBlack, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(RemInkBlack, CircleShape)
                    )
                    Text(
                        text = "SYS.V2 ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnPrimaryFixed,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Decorative Pattern Ribbon Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .background(RemInkBlack, RoundedCornerShape(2.dp))
                        .clip(RoundedCornerShape(2.dp))
                ) {
                    for (i in 0 until 18) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (i % 2 == 0) RemSurfaceContainerLowest else RemInkBlack)
                        )
                    }
                }
                Text(
                    text = "REV.44",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemOnSurfaceVariant
                )
            }

            // Brand Typography Header Unit
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mascot Logo Block
                RemMascotGraphic(size = 56.dp)

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "REM",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = RemInkBlack,
                            letterSpacing = (-1).sp
                        )
                        Box(
                            modifier = Modifier
                                .background(RemSecondaryContainer, RoundedCornerShape(4.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AI BOT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(RemPrimaryContainer, CircleShape)
                                .border(1.dp, RemInkBlack, CircleShape)
                        )
                        Text(
                            text = "SYNAPSE CORE v3.1",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemOnSurface
                        )
                    }
                }
            }

            // Slogan Banner with Highlighter Accent
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 12.dp)
                    .align(Alignment.Start)
                    .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                    .border(2.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "YOU FORGET. WE REMEMBER.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RemOnSurface,
                    letterSpacing = (-0.5).sp
                )
            }

            // Neo-Brutalist Main Hero Container
            HardShadowCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 5.dp,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Halftone retro dots top right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        HalftoneDotMatrix(rows = 3, cols = 4)
                    }

                    // Mascot Scene in Astronaut Helmet
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val cx = w / 2
                            val cy = h / 2

                            // Dashed Orbit Ring
                            drawOval(
                                color = RemInkBlack,
                                topLeft = Offset(cx - 120.dp.toPx(), cy - 40.dp.toPx()),
                                size = androidx.compose.ui.geometry.Size(240.dp.toPx(), 80.dp.toPx()),
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                )
                            )

                            // Helmet Bubble (White with black stroke)
                            drawCircle(
                                color = RemSurfaceContainerLow,
                                radius = 52.dp.toPx(),
                                center = Offset(cx, cy)
                            )
                            drawCircle(
                                color = RemInkBlack,
                                radius = 52.dp.toPx(),
                                center = Offset(cx, cy),
                                style = Stroke(3.dp.toPx())
                            )

                            // Brain Mascot Face Inside
                            drawCircle(
                                color = RemPrimaryContainer,
                                radius = 38.dp.toPx(),
                                center = Offset(cx, cy)
                            )
                            drawCircle(
                                color = RemInkBlack,
                                radius = 38.dp.toPx(),
                                center = Offset(cx, cy),
                                style = Stroke(2.5.dp.toPx())
                            )

                            // Eyes
                            drawCircle(
                                color = RemInkBlack,
                                radius = 4.5.dp.toPx(),
                                center = Offset(cx - 12.dp.toPx(), cy - 4.dp.toPx())
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 1.5.dp.toPx(),
                                center = Offset(cx - 13.dp.toPx(), cy - 6.dp.toPx())
                            )

                            drawCircle(
                                color = RemInkBlack,
                                radius = 4.5.dp.toPx(),
                                center = Offset(cx + 12.dp.toPx(), cy - 4.dp.toPx())
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 1.5.dp.toPx(),
                                center = Offset(cx + 11.dp.toPx(), cy - 6.dp.toPx())
                            )

                            // Cheerful smile
                            val mouthPath = Path().apply {
                                moveTo(cx - 10.dp.toPx(), cy + 10.dp.toPx())
                                quadraticBezierTo(cx, cy + 18.dp.toPx(), cx + 10.dp.toPx(), cy + 10.dp.toPx())
                            }
                            drawPath(mouthPath, color = RemInkBlack, style = Stroke(2.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))

                            // Glowing Amber Core Memory Box at base
                            drawRoundRect(
                                color = RemSecondaryContainer,
                                topLeft = Offset(cx - 30.dp.toPx(), cy + 36.dp.toPx()),
                                size = androidx.compose.ui.geometry.Size(60.dp.toPx(), 26.dp.toPx()),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                            )
                            drawRoundRect(
                                color = RemInkBlack,
                                topLeft = Offset(cx - 30.dp.toPx(), cy + 36.dp.toPx()),
                                size = androidx.compose.ui.geometry.Size(60.dp.toPx(), 26.dp.toPx()),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                                style = Stroke(2.dp.toPx())
                            )

                            // Core Star Icon
                            drawCircle(
                                color = RemPrimaryContainer,
                                radius = 6.dp.toPx(),
                                center = Offset(cx, cy + 49.dp.toPx())
                            )
                        }
                    }

                    // Bottom Floating Badge within Hero
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .border(1.dp, RemInkBlack.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = RemSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "NEURAL SYNC ACTIVE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RemOnSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(RemSurfaceContainer, RoundedCornerShape(4.dp))
                                .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LATENCY: 0.12ms",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RemOnSurface
                            )
                        }
                    }
                }
            }

            // Telemetry Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HardShadowCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = RemSurfaceContainerLow,
                    shadowOffset = 2.5.dp,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Storage, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("SYLLABUS BUFFER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RemOnSurfaceVariant)
                            Text("100% UNLOCKED", fontSize = 12.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
                        }
                    }
                }

                HardShadowCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = RemSurfaceContainerLow,
                    shadowOffset = 2.5.dp,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(RemSecondaryContainer, RoundedCornerShape(8.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("ENGINE STATE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RemOnSurfaceVariant)
                            Text("MARS V2 ACTIVE", fontSize = 12.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
                        }
                    }
                }
            }

            // Hazard Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = RemPrimary, modifier = Modifier.size(16.dp))
                        Text(
                            text = "INITIALIZING MARS MEMORY CORE...",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemInkBlack
                        )
                    }
                    Text(
                        text = "${(progressAnim.value * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RemSecondary
                    )
                }

                DiagonalHazardBar(progressPercent = progressAnim.value)
            }
        }

        // Action Buttons Bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StompButton(
                onClick = onBootDashboard,
                backgroundColor = RemPrimaryContainer
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = RemInkBlack)
                    Text(
                        text = "BOOT CAMPUS DASHBOARD",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = RemInkBlack,
                        letterSpacing = 0.5.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(RemInkBlack, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Sub-footer Powered By
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "POWERED BY MARS.AI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = RemInkBlack,
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(4.dp)
                        .background(RemSecondaryContainer, CircleShape)
                )
                Text(
                    text = "SPEED FOR STUDENTS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemOnSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
