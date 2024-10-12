package com.example.lumiclock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumiclock.settings.ViewModel

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        modifier = Modifier.fillMaxSize(),
        openSettings = {},
        state = ViewModel.ScreenState()
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    openSettings: () -> Unit,
    state: ViewModel.ScreenState
) {
    Box(
        modifier = modifier
            .background(color = state.currentColor)
            .padding(32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .size(50.dp)
                .clickable(onClick = openSettings),
            shape = CircleShape,
            shadowElevation = 4.5.dp
        ) {

        }
    }
}