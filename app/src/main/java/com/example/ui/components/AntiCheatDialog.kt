package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtRedDanger
import com.example.ui.theme.CbtRedLight

@Composable
fun AntiCheatDialog(
    violationsCount: Int,
    maxViolations: Int = 3,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // Non-cancelable outside click
        modifier = Modifier.testTag("anti_cheat_dialog"),
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CbtRedDanger),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dismiss_anti_cheat_button")
            ) {
                Text("Saya Mengerti & Kembali ke Ujian", fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(CbtRedLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = "Peringatan",
                        tint = CbtRedDanger,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Peringatan Pengawas!",
                    color = CbtRedDanger,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Anda terdeteksi keluar dari layar ujian CBT atau berpindah aplikasi/jendela.",
                    color = CbtNavyDark,
                    fontSize = 14.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CbtRedLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .border(1.dp, CbtRedDanger.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "Jumlah Pelanggaran: $violationsCount / $maxViolations",
                            color = CbtRedDanger,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (violationsCount >= maxViolations) {
                                "Batas pelanggaran terlampaui! Lembar ujian Anda akan otomatis dikumpulkan sekarang."
                            } else {
                                "Peringatan keras! Jika mencapai $maxViolations kali pelanggaran, ujian akan dihentikan dan jawaban otomatis terkirim."
                            },
                            color = Color(0xFF7F1D1D),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
