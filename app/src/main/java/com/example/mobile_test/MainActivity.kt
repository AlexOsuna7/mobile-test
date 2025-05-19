package com.example.mobile_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.*
import com.example.mobile_test.app.MobileTestApp
import com.example.mobile_test.features.home.presentation.FabMenu
import com.example.mobile_test.features.home.presentation.HomeScreen
import com.example.mobile_test.features.qr.presentation.QrScreen
import com.example.mobile_test.features.qr.presentation.QrViewModel
import com.example.mobile_test.features.qr.presentation.QrViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var qrViewModelFactory: QrViewModelFactory

    private val viewModel: QrViewModel by viewModels { qrViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MobileTestApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(viewModel)
        }
    }
}

@Composable
fun MyApp(viewModel: QrViewModel) {
    val navController = rememberNavController()
    var isFabMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_bar_title)) })
        },
        floatingActionButton = {
            FabMenu(
                isExpanded = isFabMenuOpen,
                onToggle = { isFabMenuOpen = !isFabMenuOpen },
                onQrClick = {
                    navController.navigate("qr_screen")
                    isFabMenuOpen = false
                },
                onScanClick = {
                    navController.navigate("scan_screen")
                    isFabMenuOpen = false
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen()
            }
            composable("qr_screen") {
                QrScreen(viewModel)
            }
            composable("scan_screen") {
                Text(stringResource(R.string.scan_screen_message))
            }
        }
    }
}