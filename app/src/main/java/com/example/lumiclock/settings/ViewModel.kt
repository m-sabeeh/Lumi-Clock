package com.example.lumiclock.settings

import android.animation.ArgbEvaluator
import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import com.example.lumiclock.ui.theme.Rainbow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ViewModel(application: Application) : AndroidViewModel(application) {

    data class ScreenState(
        val sleepColorSmooth: List<Color> = emptyList(),
        val rainbowColors: List<Color> = Rainbow.colors
    )

    val screenState = MutableStateFlow(ScreenState())

    init {
        generateGradientColors(startColor = Color(0xFF8B0000).toArgb(), endColor = Color(0xFFFFA500).toArgb(), steps = 24)
    }

    fun generateGradientColors(startColor: Int, endColor: Int, steps: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val evaluator = ArgbEvaluator()

        for (i in 0..steps) {
            val fraction = i.toFloat() / steps
            // Evaluate the color at the current step using ArgbEvaluator
            val color = evaluator.evaluate(fraction, startColor, endColor) as Int
            colors.add(color)
        }

        screenState.update {
            it.copy(
                sleepColorSmooth = colors.map {
                    Color(it)
                }
            )
        }
        return colors
    }
}