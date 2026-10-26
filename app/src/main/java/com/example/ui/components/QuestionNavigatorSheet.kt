package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CbtAmberLight
import com.example.ui.theme.CbtAmberWarning
import com.example.ui.theme.CbtGreenLight
import com.example.ui.theme.CbtGreenSuccess
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtOutline
import com.example.ui.theme.CbtTextPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuestionNavigatorSheet(
    totalQuestions: Int,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    doubtfulQuestions: Set<Int>,
    onSelectQuestion: (Int) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = modifier.testTag("question_navigator_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Lembar Navigasi Soal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )
                    Text(
                        text = "Total $totalQuestions Soal",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_navigator_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = CbtNavyDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend / Petunjuk Warna
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                    .border(1.dp, CbtOutline, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(
                    color = CbtGreenSuccess,
                    text = "Dijawab (${userAnswers.size - (userAnswers.keys.intersect(doubtfulQuestions).size)})"
                )
                LegendItem(
                    color = CbtAmberWarning,
                    text = "Ragu-ragu (${doubtfulQuestions.size})"
                )
                val unanswered = totalQuestions - userAnswers.size
                LegendItem(
                    color = Color(0xFF94A3B8),
                    text = "Kosong ($unanswered)"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid of Question Numbers
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 5
            ) {
                for (i in 0 until totalQuestions) {
                    val isCurrent = (i == currentIndex)
                    val isAnswered = userAnswers.containsKey(i)
                    val isDoubtful = doubtfulQuestions.contains(i)

                    val bgColor = when {
                        isDoubtful -> CbtAmberLight
                        isAnswered -> CbtGreenLight
                        else -> Color(0xFFF1F5F9)
                    }

                    val borderColor = when {
                        isCurrent -> CbtNavyPrimary
                        isDoubtful -> CbtAmberWarning
                        isAnswered -> CbtGreenSuccess
                        else -> CbtOutline
                    }

                    val textColor = when {
                        isDoubtful -> CbtAmberWarning
                        isAnswered -> CbtGreenSuccess
                        else -> CbtTextPrimary
                    }

                    val chosenAnswer = userAnswers[i]

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .testTag("nav_question_item_${i + 1}")
                            .clip(RoundedCornerShape(10.dp))
                            .background(bgColor)
                            .border(
                                width = if (isCurrent) 2.5.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onSelectQuestion(i) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${i + 1}",
                                fontSize = 16.sp,
                                fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = if (isCurrent) CbtNavyPrimary else textColor
                            )
                            if (chosenAnswer != null) {
                                Text(
                                    text = "($chosenAnswer)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDoubtful) CbtAmberWarning else CbtGreenSuccess
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = CbtTextPrimary
        )
    }
}
