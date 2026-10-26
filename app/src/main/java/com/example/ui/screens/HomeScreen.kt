package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CastForEducation
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamPackage
import com.example.model.Screen
import com.example.ui.theme.CbtGreenLight
import com.example.ui.theme.CbtGreenSuccess
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtNavyLight
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtOutline
import com.example.ui.theme.CbtTextPrimary
import com.example.ui.theme.CbtTextSecondary
import com.example.viewmodel.CbtViewModel

@Composable
fun HomeScreen(
    viewModel: CbtViewModel,
    modifier: Modifier = Modifier
) {
    val studentProfile by viewModel.studentProfile.collectAsState()
    val packages by viewModel.packages.collectAsState()
    val allResults by viewModel.allResults.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf("Semua") }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val categories = listOf("Semua", "UTBK-SNBT", "CAT BKN / CPNS", "Asesmen Nasional", "Ujian Sekolah")

    val filteredPackages = remember(packages, selectedCategoryFilter) {
        if (selectedCategoryFilter == "Semua") {
            packages
        } else {
            packages.filter { it.category == selectedCategoryFilter }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // App Banner & Student Identity Card
            StudentHeroHeader(
                studentName = studentProfile.fullName,
                studentId = studentProfile.idNumber,
                school = studentProfile.schoolOrInstitution,
                examRoom = studentProfile.examRoom,
                totalExamsTaken = allResults.size,
                onEditClick = { showEditProfileDialog = true }
            )
        }

        // Quick Navigation Buttons (History & Token Guide)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.History) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("nav_history_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CbtNavyPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Riwayat",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Riwayat Nilai (${allResults.size})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.AdminToken) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("nav_token_guide_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CbtNavyPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Token",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Daftar Token", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Section Title: Pilih Paket Ujian
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Paket Ujian CBT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CbtNavyDark
                )
                Text(
                    text = "${filteredPackages.size} Paket Siap",
                    fontSize = 12.sp,
                    color = CbtTextSecondary
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategoryFilter == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = category },
                        label = { Text(category, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CbtNavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Package Cards List
        items(filteredPackages, key = { it.id }) { pkg ->
            ExamPackageCard(
                pkg = pkg,
                onStartClick = { viewModel.selectPackage(pkg.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = studentProfile.fullName,
            currentId = studentProfile.idNumber,
            currentSchool = studentProfile.schoolOrInstitution,
            currentRoom = studentProfile.examRoom,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, id, school, room ->
                viewModel.updateStudentProfile(name, id, school, room)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
private fun StudentHeroHeader(
    studentName: String,
    studentId: String,
    school: String,
    examRoom: String,
    totalExamsTaken: Int,
    onEditClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("student_hero_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(CbtNavyPrimary, Color(0xFF1E40AF))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Siswa",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "PORTAL CBT ONLINE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF93C5FD),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = studentName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "No. Peserta: $studentId",
                                fontSize = 12.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }

                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                            .testTag("edit_student_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Ubah Identitas",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Metadata Pill Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = school,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = examRoom,
                            color = Color(0xFFFDE68A),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExamPackageCard(
    pkg: ExamPackage,
    onStartClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("package_card_${pkg.id}")
            .border(1.dp, CbtOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Category Badge & Token Preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pkg.category,
                        color = CbtNavyPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Token",
                        tint = CbtTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Token: ${pkg.token}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CbtNavyDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = pkg.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CbtNavyDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = pkg.description,
                fontSize = 13.sp,
                color = CbtTextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Specs Row (Questions, Duration, Passing Score)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SpecItem(
                    icon = Icons.Default.Assignment,
                    label = "Jumlah Soal",
                    value = "${pkg.questions.size} Butir"
                )
                SpecItem(
                    icon = Icons.Default.Timer,
                    label = "Durasi Waktu",
                    value = "${pkg.durationMinutes} Menit"
                )
                SpecItem(
                    icon = Icons.Default.School,
                    label = "Passing Grade",
                    value = "${pkg.passingScore}%"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("start_exam_btn_${pkg.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Mulai",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Konfirmasi & Masuk Ujian",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SpecItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = CbtNavyLight,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CbtNavyDark
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = CbtTextSecondary
        )
    }
}

@Composable
private fun EditProfileDialog(
    currentName: String,
    currentId: String,
    currentSchool: String,
    currentRoom: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var id by remember { mutableStateOf(currentId) }
    var school by remember { mutableStateOf(currentSchool) }
    var room by remember { mutableStateOf(currentRoom) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Identitas Peserta Ujian", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap Siswa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_name")
                )
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("Nomor Peserta / NISN") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_id")
                )
                OutlinedTextField(
                    value = school,
                    onValueChange = { school = it },
                    label = { Text("Sekolah / Asal Instansi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_school")
                )
                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Ruang Ujian") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_edit_room")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, id, school, room) },
                colors = ButtonDefaults.buttonColors(containerColor = CbtNavyPrimary),
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text("Simpan Data")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
