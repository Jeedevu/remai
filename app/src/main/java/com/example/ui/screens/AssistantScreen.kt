package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.repository.RemRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AssistantScreen(
    repository: RemRepository,
    onNavigateBack: () -> Unit
) {
    val messages by repository.getChatMessages().collectAsState(initial = emptyList())
    var inputText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickQuestions = listOf(
        "When is Physics due?",
        "Maths midterm details",
        "Prof Miller office hours",
        "Where is my free time gap today?"
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(RemSurface),
        containerColor = RemSurface,
        topBar = {
            AssistantTopBar(
                onBackClick = onNavigateBack,
                onClearClick = {
                    coroutineScope.launch { repository.clearChat() }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RemSurface)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quick Question Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickQuestions) { q ->
                        Box(
                            modifier = Modifier
                                .background(RemSurfaceContainerLowest, RoundedCornerShape(8.dp))
                                .border(1.5.dp, RemInkBlack, RoundedCornerShape(8.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        repository.sendChatMessage(q)
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = q,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RemOnSurface
                            )
                        }
                    }
                }

                // Chat Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ask your memory vault...", fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = RemSurfaceContainerLowest,
                            unfocusedContainerColor = RemSurfaceContainerLowest,
                            focusedBorderColor = RemInkBlack,
                            unfocusedBorderColor = RemInkBlack
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(RemPrimaryContainer, RoundedCornerShape(12.dp))
                            .border(2.dp, RemInkBlack, RoundedCornerShape(12.dp))
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    val text = inputText.trim()
                                    inputText = ""
                                    coroutineScope.launch {
                                        repository.sendChatMessage(text)
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = RemInkBlack
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessageEntity) {
    val isUser = message.sender == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isUser) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                RemMascotGraphic(size = 20.dp)
                Text("REM BRAIN BOT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemSecondary)
            }
        }

        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    if (isUser) RemPrimaryContainer else RemSurfaceContainerLowest,
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isUser) 12.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 12.dp
                    )
                )
                .border(
                    2.dp,
                    RemInkBlack,
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isUser) 12.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 12.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = RemOnSurface,
                    lineHeight = 18.sp
                )

                if (message.provenance != null) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(RemSurfaceContainer, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "📍 ${message.provenance}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = RemOnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssistantTopBar(
    onBackClick: () -> Unit,
    onClearClick: () -> Unit
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

            Column {
                Text("ASK REM", fontSize = 17.sp, fontWeight = FontWeight.Black, color = RemOnSurface)
                Text("Synapse Local Memory Engine", fontSize = 10.sp, color = RemOnSurfaceVariant)
            }
        }

        Box(
            modifier = Modifier
                .background(RemSurfaceContainerHigh, RoundedCornerShape(6.dp))
                .clickable { onClearClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("CLEAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RemOnSurface)
        }
    }
}
