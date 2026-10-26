package com.example.model

data class ExamOption(
    val id: String, // "A", "B", "C", "D", "E"
    val text: String,
    val point: Int = 0
)

data class Question(
    val id: String,
    val number: Int,
    val stimulus: String? = null,
    val question: String,
    val options: List<ExamOption>,
    val correctOptionId: String,
    val explanation: String,
    val categoryName: String
)

data class ExamPackage(
    val id: String,
    val title: String,
    val category: String,
    val durationMinutes: Int,
    val token: String,
    val questions: List<Question>,
    val passingScore: Int = 65,
    val description: String,
    val iconType: String
)

data class StudentProfile(
    val idNumber: String = "2026-CBT-0914",
    val fullName: String = "Ahmad Wijaya",
    val schoolOrInstitution: String = "SMA Negeri 1 Prestasi",
    val examRoom: String = "Lab Komputer 02",
    val session: String = "Sesi 1 (Pagi)"
)

enum class FontSizeMode(val label: String, val scaleFactor: Float) {
    SMALL("A-", 0.85f),
    NORMAL("A", 1.0f),
    LARGE("A+", 1.25f)
}

enum class QuestionStatusFilter {
    ALL,
    CORRECT,
    WRONG
}

sealed class Screen {
    object Home : Screen()
    data class Instruction(val packageId: String) : Screen()
    data class ExamRoom(val packageId: String) : Screen()
    data class Result(val resultId: Long) : Screen()
    data class Review(val packageId: String, val resultId: Long) : Screen()
    object History : Screen()
    object AdminToken : Screen()
}
