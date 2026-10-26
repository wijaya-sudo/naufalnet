package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.theme.CbtAmberWarning
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ResultScreen(
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
        viewModel.navigateTo(Screen.Home)
    }

    val result = latestResult

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
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Laporan Hasil Ujian CBT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CbtNavyDark
                )

                IconButtonBackHome { viewModel.navigateTo(Screen.Home) }
            }
        }

        if (result == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Memuat data hasil ujian...", color = CbtTextSecondary)
            }
            return
        }

        val isPassed = result.isPassed
        val statusBg = if (isPassed) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
        val statusTextColor = if (isPassed) CbtGreenSuccess else CbtRedDanger
        val statusText = if (isPassed) "LULUS / MEMENUHI PASSING GRADE" else "BELUM MEMENUHI PASSING GRADE"

        val minutes = result.timeSpentSeconds / 60
        val seconds = result.timeSpentSeconds % 60
        val timeSpentStr = "${minutes}m ${seconds}d"

        val dateFormatted = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
            .format(Date(result.timestamp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Score Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("score_hero_card")
                    .border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = result.examTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Circular Score Badge
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (isPassed) listOf(CbtNavyPrimary, Color(0xFF2563EB))
                                    else listOf(Color(0xFF991B1B), CbtRedDanger)
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${result.percentage}",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "dari 100",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Status Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(statusBg)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = statusTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Peserta: ${result.studentName} (${result.studentIdNumber})",
                        fontSize = 12.sp,
                        color = CbtTextSecondary
                    )
                    Text(
                        text = "Waktu Selesai: $dateFormatted",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            // Breakdown Grid Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rincian Jawaban & Waktu",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.CheckCircle,
                            iconColor = CbtGreenSuccess,
                            bgColor = CbtGreenLight,
                            label = "Jawaban Benar",
                            value = "${result.correctCount} Soal"
                        )
                        StatTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Cancel,
                            iconColor = CbtRedDanger,
                            bgColor = CbtRedLight,
                            label = "Jawaban Salah",
                            value = "${result.wrongCount} Soal"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Help,
                            iconColor = Color(0xFF64748B),
                            bgColor = Color(0xFFF1F5F9),
                            label = "Tidak Dijawab",
                            value = "${result.unansweredCount} Soal"
                        )
                        StatTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Timer,
                            iconColor = CbtNavyPrimary,
                            bgColor = Color(0xFFEFF6FF),
                            label = "Lama Pengerjaan",
                            value = timeSpentStr
                        )
                    }

                    if (result.violationsCount > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFEF2F2), RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Pelanggaran",
                                    tint = CbtRedDanger,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Catatan Anti-Cheat: Terdeteksi ${result.violationsCount} kali perpindahan jendela.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF991B1B),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { viewModel.navigateTo(Screen.Review(result.packageId, result.id)) },
                    colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("review_exam_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Pembahasan",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lihat Pembahasan Lengkap & Kunci",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateTo(Screen.History) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("result_to_history_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Riwayat",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Riwayat", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("result_to_home_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Beranda",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ujian Lain", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StatTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    bgColor: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CbtNavyDark
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = CbtTextSecondary
            )
        }
    }
}

@Composable
private fun IconButtonBackHome(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(34.dp)
    ) {
        Text("Beranda", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
