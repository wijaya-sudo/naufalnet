package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.model.FontSizeMode
import com.example.ui.components.AntiCheatDialog
import com.example.ui.components.ExamOptionCard
import com.example.ui.components.ExamTimerChip
import com.example.ui.components.QuestionNavigatorSheet
import com.example.ui.theme.CbtAmberLight
import com.example.ui.theme.CbtAmberWarning
import com.example.ui.theme.CbtGreenLight
import com.example.ui.theme.CbtGreenSuccess
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtNavyLight
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtOutline
import com.example.ui.theme.CbtRedDanger
import com.example.ui.theme.CbtRedLight
import com.example.ui.theme.CbtTextPrimary
import com.example.ui.theme.CbtTextSecondary
import com.example.viewmodel.CbtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamRoomScreen(
    viewModel: CbtViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPackage by viewModel.selectedPackage.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val doubtfulQuestions by viewModel.doubtfulQuestions.collectAsState()
    val remainingSeconds by viewModel.remainingTimeSeconds.collectAsState()
    val violationsCount by viewModel.violationsCount.collectAsState()
    val showViolationDialog by viewModel.showViolationDialog.collectAsState()
    val fontSizeMode by viewModel.fontSizeMode.collectAsState()
    val showNavigator by viewModel.showQuestionNavigator.collectAsState()
    val showSubmitDialog by viewModel.showSubmitConfirmDialog.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Intercept back button during exam to show confirm submit dialog
    BackHandler {
        viewModel.showSubmitDialog(true)
    }

    // Anti-cheat Lifecycle Listener: Detect when user leaves or minimizes the exam
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.triggerSuspiciousAppSwitch()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val pkg = selectedPackage ?: return
    val totalQuestions = pkg.questions.size
    val currentQuestion = pkg.questions.getOrNull(currentIndex) ?: return
    val selectedOption = userAnswers[currentIndex]
    val isDoubtful = doubtfulQuestions.contains(currentIndex)
    val scaleFactor = fontSizeMode.scaleFactor

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Top CBT App Bar (Header with Timer, Font Size, Navigator Button)
        Surface(
            color = Color.White,
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Question Number and Category
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Soal ${currentIndex + 1}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CbtNavyPrimary
                            )
                            Text(
                                text = " / $totalQuestions",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CbtTextSecondary
                            )
                        }
                        Text(
                            text = currentQuestion.categoryName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = CbtNavyLight
                        )
                    }

                    // Font Size Selector (A- / A / A+)
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                            .border(1.dp, CbtOutline, RoundedCornerShape(8.dp))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        FontSizeMode.values().forEach { mode ->
                            val isSelected = fontSizeMode == mode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) CbtNavyPrimary else Color.Transparent)
                                    .clickable { viewModel.setFontSizeMode(mode) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else CbtNavyDark
                                )
                            }
                        }
                    }

                    // Timer Chip
                    ExamTimerChip(remainingSeconds = remainingSeconds)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sub-bar: Status indicator and Navigator trigger button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Current Question Status Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isDoubtful) {
                            Box(
                                modifier = Modifier
                                    .background(CbtAmberLight, RoundedCornerShape(6.dp))
                                    .border(1.dp, CbtAmberWarning, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Ragu-Ragu",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CbtAmberWarning
                                )
                            }
                        } else if (selectedOption != null) {
                            Box(
                                modifier = Modifier
                                    .background(CbtGreenLight, RoundedCornerShape(6.dp))
                                    .border(1.dp, CbtGreenSuccess, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Dijawab ($selectedOption)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CbtGreenSuccess
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Belum Dijawab",
                                    fontSize = 11.sp,
                                    color = CbtTextSecondary
                                )
                            }
                        }

                        if (violationsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .background(CbtRedLight, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Pelanggaran: $violationsCount/3",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CbtRedDanger
                                )
                            }
                        }
                    }

                    // Open Navigator Sheet Button
                    OutlinedButton(
                        onClick = { viewModel.toggleQuestionNavigator() },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("open_question_navigator_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CbtNavyPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Daftar Soal",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Daftar Soal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Main Question Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stimulus / Reading Passage Card (if any)
            if (!currentQuestion.stimulus.isNullOrBlank()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CbtOutline, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Article,
                                contentDescription = "Stimulus Bacaan",
                                tint = CbtNavyLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bacaan / Teks Stimulus",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CbtNavyDark
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentQuestion.stimulus,
                            fontSize = (14 * scaleFactor).sp,
                            lineHeight = (22 * scaleFactor).sp,
                            color = CbtTextPrimary
                        )
                    }
                }
            }

            // Question Text Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CbtOutline.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${currentIndex + 1}. ${currentQuestion.question}",
                        fontSize = (16 * scaleFactor).sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = (24 * scaleFactor).sp,
                        color = CbtTextPrimary
                    )
                }
            }

            // Options List (A, B, C, D, E)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQuestion.options.forEach { option ->
                    ExamOptionCard(
                        option = option,
                        isSelected = selectedOption == option.id,
                        scaleFactor = scaleFactor,
                        onClick = { viewModel.selectOption(currentIndex, option.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Navigation Bar (Sebelumnya, Ragu-Ragu, Selanjutnya/Selesai)
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Sebelumnya
                OutlinedButton(
                    onClick = { viewModel.previousQuestion() },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("prev_question_button"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CbtNavyPrimary,
                        disabledContentColor = Color.LightGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Sebelumnya",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sebelumnya", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Tombol Ragu-Ragu (Amber / Yellow)
                Button(
                    onClick = { viewModel.toggleDoubtful(currentIndex) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDoubtful) CbtAmberWarning else Color(0xFFFEF3C7),
                        contentColor = if (isDoubtful) Color.White else Color(0xFF78350F)
                    ),
                    modifier = Modifier
                        .weight(1.1f)
                        .height(48.dp)
                        .testTag("doubtful_button")
                ) {
                    Icon(
                        imageVector = if (isDoubtful) Icons.Default.Check else Icons.Default.Flag,
                        contentDescription = "Ragu-Ragu",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isDoubtful) "Ragu-Ragu ✓" else "Ragu-Ragu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Tombol Selanjutnya / Selesai
                if (currentIndex < totalQuestions - 1) {
                    Button(
                        onClick = { viewModel.nextQuestion() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("next_question_button")
                    ) {
                        Text("Selanjutnya", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Selanjutnya",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.showSubmitDialog(true) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CbtGreenSuccess),
                        modifier = Modifier
                            .weight(1.1f)
                            .height(48.dp)
                            .testTag("finish_exam_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selesai",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kumpulkan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Question Navigator BottomSheet Modal
    if (showNavigator) {
        QuestionNavigatorSheet(
            totalQuestions = totalQuestions,
            currentIndex = currentIndex,
            userAnswers = userAnswers,
            doubtfulQuestions = doubtfulQuestions,
            onSelectQuestion = { viewModel.goToQuestion(it) },
            onDismiss = { viewModel.closeQuestionNavigator() },
            sheetState = sheetState
        )
    }

    // Anti-Cheat Warning Modal
    if (showViolationDialog) {
        AntiCheatDialog(
            violationsCount = violationsCount,
            maxViolations = 3,
            onDismiss = { viewModel.dismissViolationDialog() }
        )
    }

    // Submit Confirmation Dialog
    if (showSubmitDialog) {
        val answeredCount = userAnswers.size
        val doubtfulCount = doubtfulQuestions.size
        val unansweredCount = totalQuestions - answeredCount

        AlertDialog(
            onDismissRequest = { viewModel.showSubmitDialog(false) },
            modifier = Modifier.testTag("submit_confirmation_dialog"),
            title = {
                Text(
                    text = "Konfirmasi Pengumpulan Ujian",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CbtNavyDark
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Apakah Anda yakin ingin mengakhiri sesi ujian dan mengumpulkan seluruh jawaban sekarang?",
                        fontSize = 14.sp,
                        color = CbtTextPrimary
                    )

                    // Summary Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                            .border(1.dp, CbtOutline, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Soal:", fontSize = 13.sp, color = CbtTextSecondary)
                                Text("$totalQuestions Soal", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Sudah Dijawab:", fontSize = 13.sp, color = CbtGreenSuccess)
                                Text("$answeredCount Soal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CbtGreenSuccess)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Masih Ragu-Ragu:", fontSize = 13.sp, color = CbtAmberWarning)
                                Text("$doubtfulCount Soal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CbtAmberWarning)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Belum Dijawab:", fontSize = 13.sp, color = if (unansweredCount > 0) CbtRedDanger else CbtTextSecondary)
                                Text("$unansweredCount Soal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (unansweredCount > 0) CbtRedDanger else CbtTextSecondary)
                            }
                        }
                    }

                    if (unansweredCount > 0 || doubtfulCount > 0) {
                        Text(
                            text = "Perhatian: Masih terdapat soal kosong atau ragu-ragu. Jawaban yang ragu-ragu tetap akan dinilai sesuai pilihan terakhir Anda.",
                            fontSize = 12.sp,
                            color = CbtAmberWarning,
                            lineHeight = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.submitExam(isAutoTimeout = false) },
                    colors = ButtonDefaults.buttonColors(containerColor = CbtGreenSuccess),
                    modifier = Modifier.testTag("confirm_submit_exam_button")
                ) {
                    Text("Ya, Kumpulkan Sekarang", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.showSubmitDialog(false) },
                    modifier = Modifier.testTag("cancel_submit_exam_button")
                ) {
                    Text("Kembali Mengerjakan")
                }
            }
        )
    }
}
