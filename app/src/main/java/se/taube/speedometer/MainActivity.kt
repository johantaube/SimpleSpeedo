package se.taube.speedometer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import se.taube.speedometer.speedometer.SpeedometerViewModel
import se.taube.speedometer.speedometer.SpeedometerView
import se.taube.speedometer.ui.theme.SpeedometerTheme

class MainActivity : ComponentActivity() {

    private val speedometerViewModel: SpeedometerViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            speedometerViewModel.startLocationUpdates(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeedometerTheme {
                SpeedometerView(speedometerViewModel)
            }
        }

        when {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                speedometerViewModel.startLocationUpdates(this)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        speedometerViewModel.stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            speedometerViewModel.startLocationUpdates(this)
        }
    }
}