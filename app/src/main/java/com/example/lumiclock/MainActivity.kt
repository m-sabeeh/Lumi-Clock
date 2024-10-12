package com.example.lumiclock

import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import com.example.lumiclock.settings.Settings
import com.example.lumiclock.settings.ViewModel
import com.example.lumiclock.ui.theme.LumiClockTheme
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import java.util.Calendar

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<ViewModel>()
    val handler by lazy {
        Handler(mainLooper)
    }
    private var currentHour: Int = -1
    private val runnable = object : Runnable {
        override fun run() {
            val calendar = Calendar.getInstance()
            val newHour = calendar.get(Calendar.HOUR_OF_DAY)

            if (newHour != currentHour) {
                currentHour = newHour
                updateBackgroundColor()
            }
            calendar.timeInMillis = System.currentTimeMillis()
            val currentSecond = calendar.get(Calendar.SECOND)
            val currentMillisecond = calendar.get(Calendar.MILLISECOND)
            val delay = 60000 - (currentSecond * 1000 + currentMillisecond)
            // Re-run the handler at the start of the next minute
            handler.postDelayed(this, delay.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent {
            val screenState by viewModel.screenState.collectAsState()
            LumiClockTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CupcakeApp(
                        state = screenState
                    )
//                }
            }
        }
        lifecycle.addObserver(object : FullLifecycleObserverAdapter() {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                updateBackgroundColor()
                handler.post(runnable)
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                handler.removeCallbacks(runnable)
            }
        })
    }

    fun updateBackgroundColor() {
        val hourToUse = currentHour.coerceAtLeast(0).coerceAtMost(23)
        viewModel.updateCurrentColor(viewModel.screenState.value.rainbowColors[hourToUse])
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    LumiClockTheme {
        Settings(
            screenState = ViewModel.ScreenState(),
            navigateUp = {},
            canNavigateBack = true
        )
    }
}

@Preview
@Composable
fun ColorPickerScreen(modifier: Modifier = Modifier, onSave: () -> Unit = {}) {
    Column(
        modifier = modifier
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val controller = rememberColorPickerController()
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(10.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                // do something
            }
        )
        AlphaSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp),
            controller = controller,
        )
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp),
            controller = controller,
        )
        val selectedColor by controller.selectedColor
        val selectedColorText by remember(selectedColor) {
            mutableStateOf(
                String.format("#%08X", selectedColor.toArgb())
            )
        }
        Text(
            text = selectedColorText,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = selectedColor
            )
        )


        AlphaTile(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                )
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp)),
            controller = controller
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onSave,
        ) {
            Icon(
                imageVector = Icons.Filled.Create,
                contentDescription = "Save",
                tint = MaterialTheme.colorScheme.primary
            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Text(text = "Pick")

        }
    }
}