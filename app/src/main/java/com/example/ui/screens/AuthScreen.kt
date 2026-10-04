package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onNavigateBack: () -> Unit,
    onEnterVault: () -> Unit,
    onCreateAccount: () -> Unit
) {
    val scrollState = rememberScrollState()

    var studentId by remember { mutableStateOf("alex.chen@berkeley.edu") }
    var password by remember { mutableStateOf("quantum-syllabus-2026") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            // Top Utility Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(RemSurfaceContainerLowest, RoundedCornerShape(10.dp))
                        .border(2.5.dp, RemInkBlack, RoundedCornerShape(10.dp))
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RemInkBlack
                    )
                }

                Row(
                    modifier = Modifier
                        .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
                        .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RemMascotGraphic(size = 20.dp)
                    Text(
                        text = "MARS.AI STUDENT SUITE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .background(RemPrimaryContainer, RoundedCornerShape(8.dp))
                        .border(2.dp, RemInkBlack, RoundedCornerShape(8.dp))
                        .clickable { /* Help modal */ }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HELP?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnPrimaryFixed
                    )
                }
            }

            // Halftone Decorative Status Ribbon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RemInverseSurface, RoundedCornerShape(8.dp))
                    .border(2.dp, RemSecondaryContainer, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(RemPrimaryContainer, CircleShape)
                    )
                    Text(
                        text = "BRAIN_VAULT_ONLINE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemPrimaryContainer,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "v2.4 READY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemInverseOnSurface.copy(alpha = 0.8f)
                )
            }

            // Headline
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(RemSecondaryContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "⚡ AI-POWERED MEMORY ENGINE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Text(
                    text = "WELCOME BACK! READY TO",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = RemOnSurface
                )

                Box(
                    modifier = Modifier
                        .background(RemPrimaryContainer, RoundedCornerShape(4.dp))
                        .border(1.5.dp, RemInkBlack, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "DUMP YOUR BRAIN?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = RemOnSurface
                    )
                }

                Text(
                    text = "Log into your Mars.ai memory vault and let autonomous agents conquer your syllabi, deadlines, and forgotten tabs.",
                    fontSize = 13.sp,
                    color = RemOnSurfaceVariant,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Social Quick-Actions (1-Tap Auth)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Google
                HardShadowCard(
                    backgroundColor = RemSurfaceContainerLowest,
                    shadowOffset = 2.5.dp,
                    onClick = onEnterVault
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("G", fontSize = 16.sp, fontWeight = FontWeight.Black, color = RemPrimary)
                            Text("Continue with Google", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = RemOnSurface)
                    }
                }

                // University SSO
                HardShadowCard(
                    backgroundColor = RemSurfaceContainerLowest,
                    shadowOffset = 2.5.dp,
                    onClick = onEnterVault
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = RemSecondary)
                            Text("Student ID / University SSO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        }
                        Box(
                            modifier = Modifier
                                .background(RemSecondaryFixed, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("CAMPUS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RemOnSecondaryFixed)
                        }
                    }
                }

                // Apple
                HardShadowCard(
                    backgroundColor = RemSurfaceContainerLowest,
                    shadowOffset = 2.5.dp,
                    onClick = onEnterVault
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, tint = RemOnSurface)
                            Text("Sign in with Apple", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = RemOnSurface)
                    }
                }
            }

            // Divider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(RemSurfaceContainerHighest))
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(RemSecondaryContainer, RoundedCornerShape(1.dp)))
                    Text(
                        text = "OR LOGIN WITH CREDENTIALS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemOnSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                    Box(modifier = Modifier.size(6.dp).background(RemPrimaryContainer, RoundedCornerShape(1.dp)))
                }
                Box(modifier = Modifier.weight(1f).height(1.dp).background(RemSurfaceContainerHighest))
            }

            // Form Fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Email
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("STUDENT EMAIL / PHONE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        Text("EDU-VERIFIED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemOnSurfaceVariant)
                    }

                    OutlinedTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = RemOnSurface) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = RemSurfaceContainerLowest,
                            unfocusedContainerColor = RemSurfaceContainerLowest,
                            focusedBorderColor = RemInkBlack,
                            unfocusedBorderColor = RemInkBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Password
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("VAULT PASSWORD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                        Text(
                            text = "Forgot Password?",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemSecondary,
                            modifier = Modifier.clickable { }
                        )
                    }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RemOnSurface) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password",
                                    tint = RemOnSurface
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = RemSurfaceContainerLowest,
                            unfocusedContainerColor = RemSurfaceContainerLowest,
                            focusedBorderColor = RemInkBlack,
                            unfocusedBorderColor = RemInkBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Primary Enter CTA Button
                StompButton(
                    onClick = onEnterVault,
                    backgroundColor = RemPrimaryContainer,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "ENTER VAULT",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = RemInkBlack,
                        letterSpacing = 0.5.sp
                    )

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
            }

            // Mascot Speech Bubble / Tip
            HardShadowCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                backgroundColor = RemSecondaryFixed,
                shadowOffset = 2.5.dp,
                contentPadding = PaddingValues(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RemMascotGraphic(size = 36.dp)
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("MARS BOT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RemSecondary)
                            Box(
                                modifier = Modifier
                                    .background(RemPrimaryContainer, RoundedCornerShape(3.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("TIP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
                            }
                        }
                        Text(
                            text = "Logging in instantly syncs 42 active class memories and auto-solves lecture conflicts!",
                            fontSize = 12.sp,
                            color = RemOnSecondaryFixed,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // Sub-footer Security Stamp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("New to the hivemind?", fontSize = 13.sp, color = RemOnSurfaceVariant)
                Box(
                    modifier = Modifier
                        .background(RemSurfaceContainerLowest, RoundedCornerShape(6.dp))
                        .border(1.5.dp, RemInkBlack, RoundedCornerShape(6.dp))
                        .clickable { onCreateAccount() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "CREATE FREE ACCOUNT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RemSecondary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = RemPrimary, modifier = Modifier.size(14.dp))
                Text(
                    text = "CREATED BY MARS.AI // SECURE 256-BIT STUDENT ENCLAVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = RemOnSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
