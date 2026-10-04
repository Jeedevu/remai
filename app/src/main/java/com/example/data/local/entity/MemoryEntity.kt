package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val summary: String,
    val originalText: String = "",
    val category: String, // "assignments", "exams", "projects", "notes"
    val sourceType: String, // "screenshot", "pdf", "voice", "text", "link", "photo"
    val course: String = "",
    val professor: String = "",
    val deadline: String = "",
    val deadlineEpochMillis: Long = 0L,
    val importance: String = "medium", // "critical", "high", "medium", "low"
    val isStarred: Boolean = false,
    val isArchived: Boolean = false,
    val aiConfidence: Int = 98,
    val sourceDetail: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
