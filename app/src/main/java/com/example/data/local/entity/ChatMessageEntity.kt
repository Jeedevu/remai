package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String, // "user", "assistant"
    val text: String,
    val provenance: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
