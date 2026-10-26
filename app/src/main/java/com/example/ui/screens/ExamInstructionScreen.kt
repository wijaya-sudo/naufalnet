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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.theme.CbtAmberWarning
import com.example.ui.theme.CbtGreenSuccess
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtNavyLight
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtOutline
import com.example.ui.theme.CbtRedDanger
import com.example.ui.theme.CbtTextPrimary
import com.example.ui.theme.CbtTextSecondary
import com.example.viewmodel.CbtViewModel

@Composable
fun ExamInstructionScreen(
    viewModel: CbtViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPackage by viewModel.selectedPackage.collectAsState()
    val studentProfile by viewModel.studentProfile.collectAsState()
    val tokenInput by viewModel.tokenInput.collectAsState()
    val tokenError by viewModel.tokenError.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    val pkg = selectedPackage ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier.testTag("back_to_home_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali ke Beranda",
                    tint = CbtNavyDark
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Konfirmasi Tes CBT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CbtNavyDark
                )
                Text(
                    text = pkg.category,
                    fontSize = 12.sp,
                    color = CbtTextSecondary
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exam Title Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = pkg.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = pkg.description,
                        fontSize = 13.sp,
                        color = CbtTextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Alokasi Waktu", fontSize = 11.sp, color = CbtTextSecondary)
                            Text("${pkg.durationMinutes} Menit", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CbtNavyPrimary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Jumlah Soal", fontSize = 11.sp, color = CbtTextSecondary)
                            Text("${pkg.questions.size} Butir", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CbtNavyPrimary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Passing Grade", fontSize = 11.sp, color = CbtTextSecondary)
                            Text("${pkg.passingScore}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CbtNavyPrimary)
                        }
                    }
                }
            }

            // Student Verification Details
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Data Peserta",
                            tint = CbtNavyPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Verifikasi Data Peserta",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CbtNavyDark
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    StudentInfoRow("Nama Lengkap", studentProfile.fullName)
                    StudentInfoRow("Nomor Peserta", studentProfile.idNumber)
                    StudentInfoRow("Sekolah / Instansi", studentProfile.schoolOrInstitution)
                    StudentInfoRow("Ruang / Sesi", "${studentProfile.examRoom} - ${studentProfile.session}")
                }
            }

            // Tata Tertib Ujian
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Tata Tertib",
                            tint = CbtAmberWarning,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Petunjuk & Tata Tertib Ujian",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CbtNavyDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    InstructionBullet("1. Periksa kembali alokasi waktu dan butir soal sebelum menekan tombol Mulai.")
                    InstructionBullet("2. Sistem anti-cheat aktif: dilarang berpindah aplikasi atau meminimalkan jendela ujian.")
                    InstructionBullet("3. Gunakan fitur 'Ragu-Ragu' (kuning) jika ingin menandai soal untuk ditinjau kembali.")
                    InstructionBullet("4. Jawaban tersimpan otomatis. Tekan tombol Kumpulkan di akhir sesi untuk melihat nilai.")
                }
            }

            // Input Token Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Token",
                            tint = CbtNavyPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Masukkan Token Ujian",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CbtNavyDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Mintalah token sesi kepada pengawas ruang atau gunakan token simulasi di bawah:",
                        fontSize = 12.sp,
                        color = CbtTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Auto-fill button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                            .border(1.dp, CbtAmberWarning.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Token Resmi: ${pkg.token}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                        OutlinedButton(
                            onClick = { viewModel.fillSampleToken() },
                            modifier = Modifier.testTag("auto_fill_token_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF78350F))
                        ) {
                            Text("Gunakan Token Ini", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tokenInput,
                        onValueChange = { viewModel.onTokenInputChange(it) },
                        placeholder = { Text("Contoh: ${pkg.token}") },
                        isError = tokenError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.verifyTokenAndStartExam() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CbtNavyPrimary,
                            unfocusedBorderColor = CbtOutline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exam_token_input")
                    )

                    if (tokenError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tokenError ?: "",
                            color = CbtRedDanger,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Start Button
            Button(
                onClick = { viewModel.verifyTokenAndStartExam() },
                colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_exam_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Mulai",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mulai Kerjakan Ujian Sekarang",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StudentInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = CbtTextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = CbtNavyDark)
    }
}

@Composable
private fun InstructionBullet(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = CbtTextPrimary,
            lineHeight = 17.sp
        )
    }
}
