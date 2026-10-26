package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamOption
import com.example.ui.theme.CbtNavyDark
import com.example.ui.theme.CbtNavyPrimary
import com.example.ui.theme.CbtOutline
import com.example.ui.theme.CbtTextPrimary

@Composable
fun ExamOptionCard(
    option: ExamOption,
    isSelected: Boolean,
    scaleFactor: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) CbtNavyPrimary else CbtOutline,
        label = "optionBorderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFEFF6FF) else Color.White,
        label = "optionBgColor"
    )

    val circleBgColor by animateColorAsState(
        targetValue = if (isSelected) CbtNavyPrimary else Color(0xFFF1F5F9),
        label = "circleBgColor"
    )

    val circleTextColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else CbtNavyDark,
        label = "circleTextColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .testTag("option_${option.id}")
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle with Letter A, B, C, D, E
        Box(
            modifier = Modifier
                .size((36 * scaleFactor.coerceAtLeast(0.9f)).dp)
                .background(circleBgColor, CircleShape)
                .border(
                    width = 1.dp,
                    color = if (isSelected) CbtNavyPrimary else CbtOutline,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = option.id,
                color = circleTextColor,
                fontWeight = FontWeight.Bold,
                fontSize = (15 * scaleFactor).sp
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Option Text
        Text(
            text = option.text,
            color = if (isSelected) CbtNavyDark else CbtTextPrimary,
            fontSize = (15 * scaleFactor).sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            lineHeight = (22 * scaleFactor).sp,
            modifier = Modifier.weight(1f)
        )
    }
}
