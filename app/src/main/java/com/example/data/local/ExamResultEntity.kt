package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_results")
data class ExamResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageId: String,
    val examTitle: String,
    val studentName: String,
    val studentIdNumber: String,
    val score: Double,
    val maxScore: Int,
    val percentage: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val unansweredCount: Int,
    val doubtfulCount: Int,
    val timeSpentSeconds: Long,
    val timestamp: Long,
    val isPassed: Boolean,
    val userAnswersJson: String,
    val doubtfulNumbersJson: String,
    val violationsCount: Int
)
