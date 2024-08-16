package com.example.freepowersocket.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.freepowersocket.R
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable

fun ActivityCard(
    leadingSvgAsset: Int? = null,
    leadingLottieAsset: Int? = null,
    cardTitle: String? = "",
    cardSubtitle: String? = "",
    widget: @Composable () -> Unit? = {}
) {

    var leadingIcon: @Composable () -> Unit? = {};
    if (leadingLottieAsset != null) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(leadingLottieAsset))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever,
        )

        leadingIcon = {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    } else if (leadingSvgAsset != null) {
        leadingIcon = {
            Image(
                painter = painterResource(leadingSvgAsset),
                contentDescription = "leading icon",
                modifier = Modifier.padding(start = 16.dp),
                contentScale = ContentScale.Crop
            )
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.card_bg)
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon()
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = cardTitle ?: "",
                    fontSize = 18.sp,
                    color = Color.White,
                )
                Text(
                    text = cardSubtitle ?: "",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 1.dp),
                )
                widget()
            }
        }
    }
}

@Preview
@Composable
fun ActivityCardPreview() {
    FreePowerSocketTheme {
        ActivityCard(
            cardTitle = stringResource(id = R.string.scanning_for_new_devices),
            cardSubtitle = "4 Found"
        )
    }
}