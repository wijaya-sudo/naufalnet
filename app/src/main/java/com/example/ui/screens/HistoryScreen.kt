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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
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
import com.example.data.local.ExamResultEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: CbtViewModel,
    modifier: Modifier = Modifier
) {
    val allResults by viewModel.allResults.collectAsState()
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        modifier = Modifier.testTag("history_back_to_home")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Beranda",
                            tint = CbtNavyDark
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Riwayat Ujian CBT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )
                }

                if (allResults.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearConfirmDialog = true },
                        modifier = Modifier.testTag("clear_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Hapus Semua Riwayat",
                            tint = CbtRedDanger
                        )
                    }
                }
            }
        }

        if (allResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFFEFF6FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = CbtNavyPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Text(
                        text = "Belum Ada Riwayat Ujian",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )
                    Text(
                        text = "Silakan kerjakan simulasi ujian CBT di halaman beranda. Hasil dan evaluasi nilai Anda akan tersimpan di sini.",
                        fontSize = 13.sp,
                        color = CbtTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Pilih Paket Ujian")
                    }
                }
            }
        } else {
            val averageScore = remember(allResults) {
                allResults.map { it.percentage }.average().toInt()
            }
            val passedCount = remember(allResults) {
                allResults.count { it.isPassed }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Summary Stats Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total Ujian", fontSize = 11.sp, color = CbtTextSecondary)
                                Text("${allResults.size} Kali", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CbtNavyDark)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Rata-Rata Nilai", fontSize = 11.sp, color = CbtTextSecondary)
                                Text("$averageScore%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CbtNavyPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Tingkat Lulus", fontSize = 11.sp, color = CbtTextSecondary)
                                Text("$passedCount/${allResults.size}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CbtGreenSuccess)
                            }
                        }
                    }
                }

                items(allResults, key = { it.id }) { result ->
                    HistoryItemCard(
                        result = result,
                        onViewDetail = {
                            viewModel.loadResultDetail(result.id)
                            viewModel.navigateTo(Screen.Result(result.id))
                        },
                        onDelete = { viewModel.deleteResult(result.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Hapus Semua Riwayat?", fontWeight = FontWeight.Bold) },
            text = { Text("Semua catatan hasil ujian dan riwayat pengerjaan Anda akan dihapus secara permanen.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CbtRedDanger)
                ) {
                    Text("Hapus Semua")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun HistoryItemCard(
    result: ExamResultEntity,
    onViewDetail: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatted = remember(result.timestamp) {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(Date(result.timestamp))
    }
    val minutes = result.timeSpentSeconds / 60
    val seconds = result.timeSpentSeconds % 60

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormatted,
                    fontSize = 12.sp,
                    color = CbtTextSecondary
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (result.isPassed) CbtGreenLight else CbtRedLight)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (result.isPassed) "LULUS" else "BELUM LULUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (result.isPassed) CbtGreenSuccess else CbtRedDanger
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = result.examTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CbtNavyDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Score and breakdown row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Skor: ",
                        fontSize = 13.sp,
                        color = CbtTextSecondary
                    )
                    Text(
                        text = "${result.percentage}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (result.isPassed) CbtNavyPrimary else CbtRedDanger
                    )
                    Text(
                        text = " / 100",
                        fontSize = 12.sp,
                        color = CbtTextSecondary
                    )
                }

                Text(
                    text = "Benar ${result.correctCount} • Salah ${result.wrongCount} • ${minutes}m ${seconds}d",
                    fontSize = 11.sp,
                    color = CbtTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Riwayat",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onViewDetail,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buka Laporan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
