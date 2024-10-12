package com.example.lumiclock

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lumiclock.settings.Settings
import com.example.lumiclock.settings.ViewModel

/**
 * enum values that represent the screens in the app
 */
enum class LumiScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Settings(title = R.string.settings),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LumiAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    currentScreen: LumiScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        windowInsets = WindowInsets.Companion.displayCutout,
        scrollBehavior = scrollBehavior,
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp(
    state: ViewModel.ScreenState,
    navController: NavHostController = rememberNavController()
) {
//    // Get current back stack entry
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    // Get the name of the current screen
//    val currentScreen = LumiScreen.valueOf(
//        backStackEntry?.destination?.route ?: LumiScreen.Start.name

    NavHost(
        navController = navController,
        startDestination = LumiScreen.Start.name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = LumiScreen.Start.name) {
            MainScreen(
                state = state,
                openSettings = {
                    navController.navigate(LumiScreen.Settings.name)
                },
                modifier = Modifier
                    .fillMaxSize()

            )
        }
        composable(route = LumiScreen.Settings.name) {
            val context = LocalContext.current
            Settings(
                screenState = state,
                navigateUp = {
                    navController.navigateUp()
                },
                canNavigateBack = navController.previousBackStackEntry != null
            )
        }
    }
}