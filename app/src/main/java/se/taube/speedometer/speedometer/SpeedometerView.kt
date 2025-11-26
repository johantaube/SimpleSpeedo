package se.taube.speedometer.speedometer

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeedometerView(speedometerViewModel: SpeedometerViewModel) {
    val speed = speedometerViewModel.speed.collectAsState()

    SpeedometerContent(speed.value)
}

@Composable
fun SpeedometerContent(speed: Float?) {
    val pagerState = rememberPagerState(pageCount = { speedUnits.size })

    HorizontalPager(state = pagerState) { page ->
        val unit = speedUnits[page]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            speed?.let {
                val factoredSpeed = it.times(unit.m_sToUnit)
                val parts = String.format(java.util.Locale.US, "%.1f", factoredSpeed).split('.')
                val formattedSpeedMajor = parts[0]
                val formattedSpeedMinor = ".${parts.getOrNull(1) ?: '0'}"

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = formattedSpeedMajor,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 200.sp
                        ),
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        text = formattedSpeedMinor,
                        style = MaterialTheme.typography.displayMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.alignByBaseline()
                    )
                }
            } ?: Text(
                text = "---",
                style = MaterialTheme.typography.displayLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )


            Spacer(Modifier.height(24.dp))
            Text(
                text = unit.unit,
                style = MaterialTheme.typography.displayMedium.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}


private val speedUnits = listOf(
    SpeedUnit("m/s", 1f),
    SpeedUnit("km/h", 3.6f),
    SpeedUnit("knots", 1.94384f),
    SpeedUnit("ft/s", 3.28084f),
    SpeedUnit("mi/h", 2.23694f),
    SpeedUnit("mach", 0.002915f)
)

@Preview
@Composable
private fun PreviewValue() {
    SpeedometerContent(12.34f)
}

@Preview
@Composable
private fun PreviewNull() {
    SpeedometerContent(null)
}
