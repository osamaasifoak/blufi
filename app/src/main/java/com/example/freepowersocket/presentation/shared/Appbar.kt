package com.example.freepowersocket.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.freepowersocket.R
import com.example.freepowersocket.presentation.components.DeviceScanScreen
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable

fun Appbar(
    label: String,
    cardTitle: String? = "",
    cardSubtitle: String? = ""

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = label,
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)

            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.card_bg)
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = cardTitle ?: "",
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                    Text(
                        text = cardSubtitle ?: "",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 1.dp),

                        )
                    LinearProgressIndicator(
                        color = Color.White,
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .padding(top = 2.dp)
                    )
                }

            }
        }

    }
}

@Preview
@Composable
fun AppbarPreview() {
    FreePowerSocketTheme {
        Appbar(
            label = stringResource(id = R.string.connect_new_devices),
            cardTitle = stringResource(id = R.string.scanning_for_new_devices),
            cardSubtitle = "4 Found"
        )
    }
}