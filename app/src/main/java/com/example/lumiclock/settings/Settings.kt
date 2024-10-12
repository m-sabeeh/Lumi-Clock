package com.example.lumiclock.settings

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.lumiclock.LumiAppBar
import com.example.lumiclock.LumiScreen
import com.example.lumiclock.ui.theme.LumiClockTheme
import com.example.lumiclock.ui.theme.Rainbow
import com.github.skydoves.colorpicker.compose.AlphaTile

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LumiClockTheme {
        Settings(
            screenState = ViewModel.ScreenState(
                rainbowColors = Rainbow.colors,
            ),
            canNavigateBack = true,
            navigateUp = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    screenState: ViewModel.ScreenState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    val view = LocalView.current
//    if (!view.isInEditMode) {
//    SideEffect {
//        val window = (view.context as Activity).window
////        window.statusBarColor = colorScheme.primary.toArgb()
////        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
////            !darkTheme // negate darkTheme
//        ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
//            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
//            // Apply the insets as padding to the view. Here, set all the dimensions
//            // as appropriate to your layout. You can also update the view's margin if
//            // more appropriate.
//            view.updatePadding(insets.left, insets.top, insets.right, insets.bottom)
//
//            // Return CONSUMED if you don't want the window insets to keep passing down
//            // to descendant views.
//            WindowInsetsCompat.CONSUMED
//        }
//    }
//    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//        contentWindowInsets = WindowInsets.Companion.displayCutout,
        topBar = {
            LumiAppBar(
                scrollBehavior = scrollBehavior,
                currentScreen = LumiScreen.Settings,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
//                .padding(innerPadding)
                .windowInsetsPadding(
                    if (scrollBehavior.state.collapsedFraction < 1f) {
                        WindowInsets(top = innerPadding.calculateTopPadding())
                    } else {
                        WindowInsets(top = (innerPadding.calculateTopPadding() - scrollState.value.dp).coerceAtLeast(0.dp))
                    }
                )
                .verticalScroll(scrollState)
        ) {
            val radioOptions = ColorSetting.entries.toTypedArray()
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Hours Color",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                val normalBorder = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                val activeBorder = Color.Blue
                repeat(24) { index ->
                    Column(
                        Modifier
                            .defaultMinSize(minWidth = 24.dp)
                            .border(
                                width = 0.2.dp,
                                color = if (selectedOption == ColorSetting.MANUAL) {
                                    activeBorder
                                } else {
                                    normalBorder
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(enabled = selectedOption == ColorSetting.MANUAL) {
                                // do something
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format("%02d", index),
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        )
                        AlphaTile(
                            modifier = Modifier.size(height = 80.dp, width = 24.dp),
                            selectedColor = when (selectedOption) {
                                ColorSetting.RAINBOW -> screenState.rainbowColors[index]
                                ColorSetting.MANUAL -> MaterialTheme.colorScheme.primary
                                ColorSetting.SLEEP -> screenState.sleepColorSmooth[index]
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Apply Colors",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                radioOptions.forEach { setting ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (setting == selectedOption),
                                onClick = {
                                    onOptionSelected(setting)
                                }
                            )
                            .padding(horizontal = 32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            modifier = Modifier.weight(1f, true),
                            text = setting.settingString,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary
                            ),
                        )
                        RadioButton(
                            selected = (setting == selectedOption),
                            onClick = { onOptionSelected(setting) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Sleep Settings",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = "Sleep Time",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .border(
                            width = 0.2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .clickable() {
                            // do something
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "Start",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "12:00 PM",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .border(
                            width = 0.2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .clickable() {
                            // do something
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "End",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "02:00 PM",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = "Colors",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .border(
                            width = 0.2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .clickable() {
                            // do something
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "Sleep Color",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AlphaTile(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        selectedColor = Color(0xFF8B0000)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Column(
                    modifier = Modifier
                        .border(
                            width = 0.2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .clickable() {
                            // do something
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "Awake Color",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AlphaTile(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        selectedColor = Color(0xFFFFA500)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.padding(6.dp))
            val checkedState = remember { mutableStateOf(true) }
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
                Text(
                    text = "Smooth Transition",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.End),
                onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = "Color Transition",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            //Repated code
            Spacer(modifier = Modifier.padding(6.dp))
            val checkedStates = remember { mutableStateOf(true) }
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
                Text(
                    text = "Smooth Transition",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .align(Alignment.End),
                onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = "Color Transition",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

enum class ColorSetting(val settingString: String) {
    SLEEP("Sleep Settings"),
    RAINBOW("Rainbow"),
    MANUAL("Manual")
}