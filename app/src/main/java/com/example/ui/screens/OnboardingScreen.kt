package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HalftoneDotMatrix(rows = 2, cols = 4)
                    Box(
                        modifier = Modifier
                            .background(RemSurfaceContainer, RoundedCornerShape(4.dp))
                            .border(1.dp, RemInkBlack.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "SYS.V1 // READY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemOnSurface
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(RemSurfaceContainerHigh, RoundedCornerShape(8.dp))
                        .clickable { onGetStarted() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "SKIP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RemOnSurface,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Hero Mascot Graphic Frame
            HardShadowCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                backgroundColor = RemSurfaceContainerLowest,
                shadowOffset = 4.dp,
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Top Left Status Badge
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(RemSurface, CircleShape)
                            .border(1.dp, RemInkBlack.copy(alpha = 0.2f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(RemSecondaryContainer, CircleShape)
                        )
                        Text(
                            text = "MEMORY ENGINE ONLINE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemOnSurface
                        )
                    }

                    // Center Mascot
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        RemMascotGraphic(size = 90.dp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .background(RemPrimaryContainer, RoundedCornerShape(6.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "WELCOME TO BRAIN BOOST!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = RemInkBlack
                            )
                        }
                    }

                    // Bottom Right Retention Badge
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(RemPrimaryContainer, RoundedCornerShape(6.dp))
                            .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Memory, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(14.dp))
                        Text(
                            text = "100% RETENTION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = RemInkBlack
                        )
                    }
                }
            }

            // Headline Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(RemSecondaryContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "ANTI-PROCRASTINATION AI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "STOP FORGETTING.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface,
                    lineHeight = 32.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "YOUR BRAIN HAS",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = RemOnSurface
                        )
                    }
                }

                Text(
                    text = "ENOUGH TO DO.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface,
                    lineHeight = 32.sp
                )

                Text(
                    text = "Your personal AI memory automatically recalls assignments, exams, pop quizzes, and unhinged syllabus updates directly from your screenshots and messy camera roll.",
                    fontSize = 13.sp,
                    color = RemOnSurfaceVariant,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Pill Feature Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                        .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = RemInkBlack, modifier = Modifier.size(14.dp))
                    Text("Drop Screenshots", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemInkBlack)
                }

                Row(
                    modifier = Modifier
                        .background(RemSecondary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Text("Read Syllabi & PDFs", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(RemTertiary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Text("Auto-Extract Tasks", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            // Dots Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(8.dp)
                        .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                        .border(1.dp, RemInkBlack, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(RemSurfaceContainerHighest, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(RemSurfaceContainerHighest, CircleShape)
                )
            }
        }

        // Bottom CTA
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StompButton(
                onClick = onGetStarted,
                backgroundColor = RemPrimaryContainer
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = RemInkBlack)
                    Text(
                        text = "LET'S GO",
                        fontSize = 15.sp,
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

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 13.sp,
                    color = RemOnSurfaceVariant
                )
                Text(
                    text = "SIGN IN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemSecondary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onSignIn() }
                )
            }
        }
    }
}
