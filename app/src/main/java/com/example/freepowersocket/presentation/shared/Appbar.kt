package com.example.freepowersocket.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freepowersocket.R
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable
fun Appbar(
    label: String,
    widget: @Composable () -> Unit? = {}

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(266.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(266.dp)
                .background(color = Color.Black)
                .alpha(0.6f)
        ) {
            Image(
                painter = painterResource(R.drawable.product_close),
                contentDescription = "product banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,

        ) {
            Text(
                text = label,
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 32.dp)

            )
            widget()
        }
    }
}

@Preview
@Composable
fun AppbarPreview() {
    FreePowerSocketTheme {
        Appbar(
            label = stringResource(id = R.string.connect_new_devices),
            widget = {
                ActivityCard(
                    cardSubtitle = stringResource(id = R.string.scanning_for_new_devices),
                    cardTitle = "4 Found"
                )
            }
        )
    }
}