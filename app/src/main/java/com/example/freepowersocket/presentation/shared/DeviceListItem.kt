package com.example.freepowersocket.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable
fun DeviceListItem(
    label: String,
    subTitle: String,
    hadTopDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        if (hadTopDivider)
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.background(color = Color.Gray).alpha(0.6f)
            )
        Box(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(text = subTitle, color = Color.Black)
            }
        }
        HorizontalDivider(
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
fun DeviceListItemPreview() {
    FreePowerSocketTheme {
        DeviceListItem(label = "Device 1", subTitle = "00:00:00:00")
    }
}