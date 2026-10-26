package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExamDataProvider
import com.example.model.Screen
import com.example.ui.theme.CbtGreenLight
import com.example.ui.theme.CbtGreenSuccess
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtOutline
import com.example.ui.theme.CbtRedDanger
import com.example.ui.theme.CbtRedLight
import com.example.ui.theme.CbtTextPrimary
import com.example.ui.theme.CbtTextSecondary
import com.example.viewmodel.CbtViewModel
import org.json.JSONObject

@Composable
fun ReviewScreen(
    packageId: String,
    resultId: Long,
    viewModel: CbtViewModel,
    modifier: Modifier = Modifier
) {
    val latestResult by viewModel.latestResult.collectAsState()

    LaunchedEffect(resultId) {
        if (latestResult == null || latestResult?.id != resultId) {
            viewModel.loadResultDetail(resultId)
        }
    }

    BackHandler {
        viewModel.navigateTo(Screen.Result(resultId))
    }

    val pkg = ExamDataProvider.getPackageById(packageId)
    val result = latestResult

    if (pkg == null || result == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Memuat data pembahasan...", color = CbtTextSecondary)
        }
        return
    }

    // Parse user answers JSON
    val answersMap = remember(result.userAnswersJson) {
        val map = mutableMapOf<Int, String>()
        try {
            val json = JSONObject(result.userAnswersJson)
            val keys = json.keys()
            while (keys.hasNext()) {
                val k = keys.next()
                map[k.toInt()] = json.getString(k)
            }
        } catch (_: Exception) {}
        map
    }

    var currentReviewIndex by remember { mutableStateOf(0) }
    val currentQuestion = pkg.questions.getOrNull(currentReviewIndex) ?: return

    val totalQuestions = pkg.questions.size
    val userSelectedOption = answersMap[currentReviewIndex]
    val correctOption = currentQuestion.correctOptionId
    val isCorrect = userSelectedOption.equals(correctOption, ignoreCase = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // App Bar
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Result(resultId)) },
                    modifier = Modifier.testTag("back_to_result_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali ke Hasil",
                        tint = CbtNavyDark
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Pembahasan & Kunci Jawaban",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )
                    Text(
                        text = pkg.title,
                        fontSize = 12.sp,
                        color = CbtTextSecondary
                    )
                }
            }
        }

        // Horizontal Number Selector Strip
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(pkg.questions) { index, q ->
                val ans = answersMap[index]
                val right = ans.equals(q.correctOptionId, ignoreCase = true)
                val isSelected = (index == currentReviewIndex)

                val badgeBg = when {
                    ans == null -> Color(0xFFF1F5F9)
                    right -> CbtGreenLight
                    else -> CbtRedLight
                }
                val badgeText = when {
                    ans == null -> Color.Gray
                    right -> CbtGreenSuccess
                    else -> CbtRedDanger
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) CbtNavyPrimary else CbtOutline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { currentReviewIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (isSelected) CbtNavyPrimary else badgeText
                    )
                }
            }
        }

        // Scrollable Question Review Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Soal Nomor ${currentReviewIndex + 1} of $totalQuestions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CbtNavyDark
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCorrect) CbtGreenLight else CbtRedLight)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isCorrect) CbtGreenSuccess else CbtRedDanger,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isCorrect) "Jawaban Benar (+1)" else if (userSelectedOption == null) "Tidak Dijawab (0)" else "Jawaban Salah (0)",
                            color = if (isCorrect) CbtGreenSuccess else CbtRedDanger,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Stimulus Card (if any)
            if (!currentQuestion.stimulus.isNullOrBlank()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Article,
                                contentDescription = null,
                                tint = CbtNavyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Teks Bacaan / Stimulus",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CbtNavyDark
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQuestion.stimulus,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = CbtTextPrimary
                        )
                    }
                }
            }

            // Question Text Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "${currentReviewIndex + 1}. ${currentQuestion.question}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
                        color = CbtTextPrimary
                    )
                }
            }

            // Options Review
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                currentQuestion.options.forEach { option ->
                    val isThisCorrect = option.id.equals(correctOption, ignoreCase = true)
                    val isThisUserChoice = option.id.equals(userSelectedOption, ignoreCase = true)

                    val borderCol = when {
                        isThisCorrect -> CbtGreenSuccess
                        isThisUserChoice -> CbtRedDanger
                        else -> CbtOutline
                    }

                    val bgCol = when {
                        isThisCorrect -> Color(0xFFD1FAE5)
                        isThisUserChoice -> Color(0xFFFEE2E2)
                        else -> Color.White
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(bgCol)
                            .border(width = if (isThisCorrect || isThisUserChoice) 2.dp else 1.dp, color = borderCol, shape = RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    when {
                                        isThisCorrect -> CbtGreenSuccess
                                        isThisUserChoice -> CbtRedDanger
                                        else -> Color(0xFFE2E8F0)
                                    },
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option.id,
                                color = if (isThisCorrect || isThisUserChoice) Color.White else CbtNavyDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.text,
                                fontSize = 14.sp,
                                color = CbtTextPrimary
                            )
                            if (isThisCorrect) {
                                Text(
                                    text = "✓ KUNCI JAWABAN BENAR",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CbtGreenSuccess
                                )
                            } else if (isThisUserChoice) {
                                Text(
                                    text = "✗ JAWABAN ANDA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CbtRedDanger
                                )
                            }
                        }
                    }
                }
            }

            // Explanation & Solution Box
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Pembahasan",
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pembahasan Lengkap & Konsep Cepat",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentQuestion.explanation,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Bottom Prev / Next Navigation Strip
        Surface(
            color = Color.White,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { if (currentReviewIndex > 0) currentReviewIndex-- },
                    enabled = currentReviewIndex > 0,
                    modifier = Modifier.testTag("review_prev_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Sebelumnya",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sebelumnya")
                }

                Text(
                    text = "${currentReviewIndex + 1} / $totalQuestions",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CbtNavyDark
                )

                Button(
                    onClick = { if (currentReviewIndex < totalQuestions - 1) currentReviewIndex++ },
                    enabled = currentReviewIndex < totalQuestions - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                    modifier = Modifier.testTag("review_next_button")
                ) {
                    Text("Selanjutnya")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Selanjutnya",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
