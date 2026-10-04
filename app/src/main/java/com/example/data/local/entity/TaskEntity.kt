package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memoryId: Long? = null,
    val title: String,
    val subtitle: String = "",
    val estimatedMinutes: Int = 30,
    val priority: String = "MED PRIORITY", // "CRITICAL", "HIGH PRIORITY", "MED PRIORITY", "LOW"
    val status: String = "TODO", // "TODO", "IN_PROGRESS", "COMPLETED", "SNOOZED"
    val isCompleted: Boolean = false,
    val dueDisplay: String = "",
    val recommendedSlot: String = "",
    val isNext: Boolean = false,
    val orderIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
