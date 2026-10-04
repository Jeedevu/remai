package com.example

import com.example.data.ai.RemAiEngine
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun aiEngine_parsesPhysicsInputCorrectly() {
        val raw = "Physics Chapter 4 Problem Set due tomorrow 8 PM. Bring free-body diagrams."
        val result = RemAiEngine.parseInput("screenshot", raw)

        assertEquals("Physics 101", result.course)
        assertEquals("HIGH PRIORITY", result.priority)
        assertEquals("assignments", result.category)
        assertTrue(result.confidence in 90..100)
        assertTrue(result.candidateTasks.isNotEmpty())
    }

    @Test
    fun aiEngine_parsesMathMidtermCorrectly() {
        val raw = "Maths Midterm Exam Calculus Friday 9 AM covering Ch. 4-6"
        val result = RemAiEngine.parseInput("text", raw)

        assertEquals("Mathematics (Calc)", result.course)
        assertEquals("CRITICAL", result.priority)
        assertEquals("exams", result.category)
        assertEquals("Due Friday, 9:00 AM", result.deadline)
    }

    @Test
    fun aiEngine_parsesProfMillerOfficeHoursCorrectly() {
        val raw = "Prof Miller Office Hours Thurs 3pm in Room 302"
        val result = RemAiEngine.parseInput("link", raw)

        assertEquals("Prof. Miller", result.professor)
        assertEquals("CS 101", result.course)
        assertEquals("Due Thursday, 3:00 PM", result.deadline)
    }
}
