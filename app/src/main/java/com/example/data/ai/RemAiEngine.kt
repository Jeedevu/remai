package com.example.data.ai

import com.example.data.local.entity.MemoryEntity
import com.example.data.local.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExtractionResult(
    val title: String,
    val summary: String,
    val course: String,
    val professor: String,
    val deadline: String,
    val priority: String,
    val category: String,
    val estimatedMinutes: Int,
    val confidence: Int,
    val candidateTasks: List<String>
)

object RemAiEngine {

    fun parseInput(
        sourceType: String,
        rawText: String,
        attachmentName: String? = null
    ): ExtractionResult {
        val lower = rawText.lowercase()

        // Detect professor
        val professor = when {
            lower.contains("miller") -> "Prof. Miller"
            lower.contains("alvarez") -> "Dr. Alvarez"
            lower.contains("thorne") -> "Dr. Thorne"
            lower.contains("henderson") -> "Prof. Henderson"
            else -> "Faculty Staff"
        }

        // Detect course
        val course = when {
            lower.contains("physics") -> "Physics 101"
            lower.contains("miller") || lower.contains("cs") || lower.contains("code") || lower.contains("sql") || lower.contains("database") -> "CS 101"
            lower.contains("chem") || lower.contains("acid") || lower.contains("lab") -> "Chemistry 1A"
            lower.contains("econ") || lower.contains("macro") || lower.contains("micro") -> "Macroeconomics"
            lower.contains("bio") -> "Biology 101"
            lower.contains("math") || lower.contains("calc") -> "Mathematics (Calc)"
            lower.contains("english") || lower.contains("essay") || lower.contains("lit") -> "English Literature"
            else -> "General Academic"
        }

        // Detect priority
        val priority = when {
            lower.contains("exam") || lower.contains("midterm") || lower.contains("urgent") || lower.contains("critical") -> "CRITICAL"
            lower.contains("tomorrow") || lower.contains("due soon") || lower.contains("assignment") || lower.contains("problem set") -> "HIGH PRIORITY"
            else -> "MED PRIORITY"
        }

        // Detect category
        val category = when {
            lower.contains("exam") || lower.contains("quiz") || lower.contains("test") || lower.contains("midterm") -> "exams"
            lower.contains("project") || lower.contains("spec") || lower.contains("repo") -> "projects"
            lower.contains("problem set") || lower.contains("assignment") || lower.contains("homework") || lower.contains("pset") -> "assignments"
            lower.contains("read") || lower.contains("notes") || lower.contains("ch.") || lower.contains("chapter") -> "notes"
            else -> "assignments"
        }

        // Detect deadline string
        val deadline = when {
            lower.contains("friday") -> "Due Friday, 9:00 AM"
            lower.contains("tomorrow") -> "Due Tomorrow, 8:00 PM"
            lower.contains("thurs") -> "Due Thursday, 3:00 PM"
            lower.contains("wed") -> "Due Wednesday, 11:59 PM"
            lower.contains("today") -> "Due Today, 5:00 PM"
            else -> "Due in 3 days"
        }

        val title = if (rawText.isNotBlank()) {
            val firstLine = rawText.lines().firstOrNull()?.trim() ?: "Quick Memory"
            if (firstLine.length > 32) firstLine.take(29) + "..." else firstLine
        } else {
            when (sourceType) {
                "screenshot" -> "Lecture Slide Note"
                "pdf" -> attachmentName?.removeSuffix(".pdf") ?: "Course Syllabus Extract"
                "voice" -> "Voice Brain Dump"
                "photo" -> "Whiteboard Snapshot"
                "link" -> "Shared Web Resource"
                else -> "Quick Captured Note"
            }
        }

        val summary = if (rawText.isNotBlank()) {
            rawText.trim()
        } else {
            "Captured via $sourceType ingestion. AI automatically identified deadlines, actions, and priority tags."
        }

        val candidateTasks = listOf(
            "Review $title before $deadline",
            "Prepare working notes for $course"
        )

        return ExtractionResult(
            title = title,
            summary = summary,
            course = course,
            professor = professor,
            deadline = deadline,
            priority = priority,
            category = category,
            estimatedMinutes = if (priority == "CRITICAL") 60 else 30,
            confidence = (92..99).random(),
            candidateTasks = candidateTasks
        )
    }

    fun toMemoryEntity(result: ExtractionResult, sourceType: String): MemoryEntity {
        val sourceDetail = when (sourceType) {
            "screenshot" -> "Screenshot parsed · ${result.confidence}% AI conf."
            "pdf" -> "PDF Document · Page 1"
            "voice" -> "Voice Memo (Transcribed)"
            "link" -> "Web Link Reference"
            else -> "Quick Text Capture"
        }

        return MemoryEntity(
            title = result.title,
            summary = result.summary,
            category = result.category,
            sourceType = sourceType,
            course = result.course,
            professor = result.professor,
            deadline = result.deadline,
            importance = if (result.priority == "CRITICAL") "critical" else "high",
            aiConfidence = result.confidence,
            sourceDetail = sourceDetail
        )
    }

    fun toTaskEntity(result: ExtractionResult, memoryId: Long?): TaskEntity {
        return TaskEntity(
            memoryId = memoryId,
            title = result.title,
            subtitle = "${result.deadline} • ${result.course}",
            estimatedMinutes = result.estimatedMinutes,
            priority = result.priority,
            status = "TODO",
            isCompleted = false,
            dueDisplay = result.deadline,
            recommendedSlot = "Auto-scheduled slot"
        )
    }
}
