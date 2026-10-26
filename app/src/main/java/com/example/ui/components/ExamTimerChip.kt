package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CbtAmberWarning
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtRedDanger
import java.util.Locale

@Composable
fun ExamTimerChip(
    remainingSeconds: Long,
    modifier: Modifier = Modifier
) {
    val isCritical = remainingSeconds in 1..60
    val isWarning = remainingSeconds in 61..300

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCritical -> Color(0xFFFEE2E2)
            isWarning -> Color(0xFFFEF3C7)
            else -> Color(0xFFEFF6FF)
        },
        label = "timerBgColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isCritical -> CbtRedDanger
            isWarning -> CbtAmberWarning
            else -> CbtNavyPrimary
        },
        label = "timerContentColor"
    )

    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    val timeFormatted = if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    Row(
        modifier = modifier
            .testTag("exam_timer_chip")
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, contentColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = if (isCritical) Icons.Default.Warning else Icons.Default.Alarm,
            contentDescription = "Timer Ujian",
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = timeFormatted,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
