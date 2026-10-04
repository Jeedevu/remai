package com.example.data.repository

import android.content.Context
import com.example.data.local.RemDatabase
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.MemoryEntity
import com.example.data.local.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemRepository(private val database: RemDatabase) {

    private val memoryDao = database.memoryDao()
    private val taskDao = database.taskDao()
    private val chatDao = database.chatDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val count = memoryDao.getAllMemories().first()
        if (count.isEmpty()) {
            val initialMemories = listOf(
                MemoryEntity(
                    title = "Physics Module 4",
                    summary = "Complete questions 1–10 from mechanics problem set. Don't forget free-body diagrams for harmonic oscillators.",
                    category = "assignments",
                    sourceType = "screenshot",
                    course = "Physics 101",
                    professor = "Dr. Alvarez",
                    deadline = "Due: 08 Oct",
                    importance = "high",
                    aiConfidence = 98,
                    sourceDetail = "Screenshot parsed · 98% AI conf."
                ),
                MemoryEntity(
                    title = "CS 101 Project Spec",
                    summary = "Build a relational database schema for student portal with 3NF tables and indexed foreign keys.",
                    category = "projects",
                    sourceType = "pdf",
                    course = "Computer Science 101",
                    professor = "Prof. Miller",
                    deadline = "Due: 15 Nov",
                    importance = "high",
                    aiConfidence = 96,
                    sourceDetail = "Syllabus.pdf (Page 4)"
                ),
                MemoryEntity(
                    title = "Chem Lab Protocol",
                    summary = "Wear safety goggles, titrate hydrochloric acid sample, and calculate titration curves before submission.",
                    category = "exams",
                    sourceType = "voice",
                    course = "Chemistry 1A",
                    professor = "Dr. Thorne",
                    deadline = "Due: Thursday",
                    importance = "critical",
                    aiConfidence = 94,
                    sourceDetail = "Voice Memo (0:45s)"
                ),
                MemoryEntity(
                    title = "Macro Econ Ch. 5",
                    summary = "Key concepts: Elasticity of demand and consumer surplus. Highlight marginal utility graphs for quiz review.",
                    category = "notes",
                    sourceType = "link",
                    course = "Macroeconomics",
                    professor = "Prof. Henderson",
                    deadline = "Reading",
                    importance = "medium",
                    aiConfidence = 99,
                    sourceDetail = "Canvas Web Link"
                )
            )
            memoryDao.insertMemories(initialMemories)

            val initialTasks = listOf(
                TaskEntity(
                    title = "Review Bio Notes",
                    subtitle = "Session 10:00 AM • Cellular Respiration",
                    estimatedMinutes = 20,
                    priority = "MED PRIORITY",
                    status = "COMPLETED",
                    isCompleted = true,
                    dueDisplay = "Today, 10:00 AM",
                    recommendedSlot = "Session 10:00 AM",
                    orderIndex = 1
                ),
                TaskEntity(
                    title = "Physics Problem Set",
                    subtitle = "Recommended 2:00 PM slot",
                    estimatedMinutes = 45,
                    priority = "HIGH PRIORITY",
                    status = "TODO",
                    isCompleted = false,
                    isNext = true,
                    dueDisplay = "Due Tomorrow, 8:00 PM",
                    recommendedSlot = "Recommended 2:00 PM slot",
                    orderIndex = 2
                ),
                TaskEntity(
                    title = "Group Sync: AI Ethics Lab",
                    subtitle = "Zoom Call • 4:30 PM",
                    estimatedMinutes = 30,
                    priority = "MED PRIORITY",
                    status = "TODO",
                    isCompleted = false,
                    dueDisplay = "Today, 4:30 PM",
                    recommendedSlot = "Zoom Call • 4:30 PM",
                    orderIndex = 3
                ),
                TaskEntity(
                    title = "English Essay Draft",
                    subtitle = "Due Wed, 11:59 PM • Lit Review",
                    estimatedMinutes = 60,
                    priority = "MED PRIORITY",
                    status = "TODO",
                    isCompleted = false,
                    dueDisplay = "Due Wed, 11:59 PM",
                    orderIndex = 4
                ),
                TaskEntity(
                    title = "Maths Midterm Exam",
                    subtitle = "Due Friday, 9:00 AM • Calc Ch. 4-6",
                    estimatedMinutes = 90,
                    priority = "CRITICAL",
                    status = "TODO",
                    isCompleted = false,
                    dueDisplay = "Due Friday, 9:00 AM",
                    orderIndex = 5
                )
            )
            taskDao.insertTasks(initialTasks)

            // Seed initial AI assistant chat
            chatDao.insertMessage(
                ChatMessageEntity(
                    sender = "assistant",
                    text = "Hey Alex! I'm REM, your student AI brain. I've indexed your syllabi, screenshots, and lab notes. What do you need to remember or review right now?",
                    provenance = "Synapse Core v3.1"
                )
            )
        }
    }

    // Memory APIs
    fun getAllMemories(): Flow<List<MemoryEntity>> = memoryDao.getAllMemories()
    fun getMemoriesByCategory(category: String): Flow<List<MemoryEntity>> =
        if (category == "all") memoryDao.getAllMemories() else memoryDao.getMemoriesByCategory(category)
    fun searchMemories(query: String): Flow<List<MemoryEntity>> = memoryDao.searchMemories(query)
    suspend fun insertMemory(memory: MemoryEntity): Long = memoryDao.insertMemory(memory)
    suspend fun toggleStar(id: Long, starred: Boolean) = memoryDao.toggleStar(id, starred)
    suspend fun deleteMemory(id: Long) = memoryDao.deleteMemory(id)
    suspend fun clearAllMemories() = memoryDao.clearAll()

    // Task APIs
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    fun getActiveTasks(): Flow<List<TaskEntity>> = taskDao.getActiveTasks()
    fun getUrgentTasks(): Flow<List<TaskEntity>> = taskDao.getUrgentTasks()
    fun getCompletedCount(): Flow<Int> = taskDao.getCompletedCount()
    fun getTotalCount(): Flow<Int> = taskDao.getTotalCount()
    suspend fun toggleTaskComplete(id: Long, completed: Boolean) = taskDao.setTaskCompleted(id, completed)
    suspend fun insertTask(task: TaskEntity): Long = taskDao.insertTask(task)
    suspend fun deleteTask(id: Long) = taskDao.deleteTask(id)

    // Chat APIs
    fun getChatMessages(): Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()
    suspend fun sendChatMessage(text: String, provenance: String? = null) {
        chatDao.insertMessage(ChatMessageEntity(sender = "user", text = text))
        withContext(Dispatchers.IO) {
            val response = generateAssistantResponse(text)
            chatDao.insertMessage(response)
        }
    }
    suspend fun clearChat() = chatDao.clearChat()

    private fun generateAssistantResponse(userQuery: String): ChatMessageEntity {
        val q = userQuery.lowercase()
        return when {
            q.contains("physics") -> ChatMessageEntity(
                sender = "assistant",
                text = "Physics Module 4 is due tomorrow at 8:00 PM (Questions 1–10, harmonic oscillators). Also, your Neural Radar reserved 2:00 PM–4:00 PM today for your Physics Sprint!",
                provenance = "Physics Module 4 Screenshot (98% Conf)"
            )
            q.contains("exam") || q.contains("math") || q.contains("midterm") -> ChatMessageEntity(
                sender = "assistant",
                text = "Your Maths Midterm Exam is this Friday at 9:00 AM covering Calculus Chapters 4–6. It's tagged CRITICAL in your alert pipeline.",
                provenance = "Math Syllabus PDF"
            )
            q.contains("miller") || q.contains("office hours") -> ChatMessageEntity(
                sender = "assistant",
                text = "Prof. Miller's Office Hours are Thursday at 3:00 PM (CS Department Hall, Room 302).",
                provenance = "Quick Clipboard Note"
            )
            q.contains("free time") || q.contains("gap") || q.contains("radar") -> ChatMessageEntity(
                sender = "assistant",
                text = "Neural Radar detected an open 2-hour window between 2:00 PM and 4:00 PM right after your lecture. Perfect for knocking out high-focus tasks.",
                provenance = "Neural Radar Schedule Engine"
            )
            q.contains("tomorrow") || q.contains("due") -> ChatMessageEntity(
                sender = "assistant",
                text = "Tomorrow you have:\n1. Physics Assignment (Due 8:00 PM)\n2. English Essay Draft (Wed 11:59 PM)\nWould you like me to schedule focus blocks for either?",
                provenance = "Upcoming Deadlines Filter"
            )
            else -> ChatMessageEntity(
                sender = "assistant",
                text = "I've scanned your student memory vault for \"$userQuery\". You have 42 indexed memories across Physics, CS 101, Chemistry, and Macroeconomics. Let me know if you want to create a task or block focus time!",
                provenance = "Synapse Memory Index"
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RemRepository? = null

        fun getInstance(context: Context): RemRepository {
            return INSTANCE ?: synchronized(this) {
                val db = RemDatabase.getInstance(context)
                val repo = RemRepository(db)
                INSTANCE = repo
                repo
            }
        }
    }
}
