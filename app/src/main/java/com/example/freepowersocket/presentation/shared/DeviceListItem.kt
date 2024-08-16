package com.example.freepowersocket.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable
fun DeviceListItem(
    trailingLottieAsset: Int? = null,
    trailingSvgAsset: Int? = null,
    label: String,
    subTitle: String,
    hadTopDivider: Boolean = true,
    modifier: Modifier = Modifier
) {

    var trailingIcon: @Composable () -> Unit? = {};
    if (trailingLottieAsset != null) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                trailingLottieAsset
            )
        )
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever,
        )

        trailingIcon = {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    } else if (trailingSvgAsset != null) {
        trailingIcon = {
            Image(
                painter = painterResource(trailingSvgAsset),
                contentDescription = "trailing icon",
                modifier = Modifier.padding(horizontal = 16.dp).height(32.dp).width(32.dp),
                contentScale = ContentScale.Fit
            )
        }
    }

    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        if (hadTopDivider)
            HorizontalDivider(
                thickness = 0.5.dp,
                color = Color.Gray,
            )
        Box(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
                trailingIcon()
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray,
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