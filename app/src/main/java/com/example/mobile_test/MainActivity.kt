package com.example.mobile_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile_test.app.MobileTestApp
import com.example.mobile_test.features.home.presentation.FabMenu
import com.example.mobile_test.features.home.presentation.HomeScreen
import com.example.mobile_test.features.qr.presentation.QrScreen
import com.example.mobile_test.features.qr.presentation.QrViewModel
import com.example.mobile_test.features.qr.presentation.QrViewModelFactory
import com.example.mobile_test.features.scanner.presentation.ScannerScreen
import com.example.mobile_test.features.scanner.presentation.ScannerViewModel
import com.example.mobile_test.features.scanner.presentation.ScannerViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var qrViewModelFactory: QrViewModelFactory

    @Inject
    lateinit var scannerViewModelFactory: ScannerViewModelFactory

    private val qrViewModel: QrViewModel by viewModels { qrViewModelFactory }
    private val scannerViewModel: ScannerViewModel by viewModels { scannerViewModelFactory }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MobileTestApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

            LaunchedEffect(Unit) {
                if (!cameraPermissionState.status.isGranted) {
                    cameraPermissionState.launchPermissionRequest()
                }
            }

            if (cameraPermissionState.status.isGranted) {
                MyApp(qrViewModel, scannerViewModel)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PermissionDeniedScreen {
                        cameraPermissionState.launchPermissionRequest()
                    }                }
            }
        }
    }
}

@Composable
fun MyApp(qrViewModel: QrViewModel, scannerViewModel: ScannerViewModel) {
    val navController = rememberNavController()
    var isFabMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { MyAppTopBar(navController) },
        floatingActionButton = {
            FabMenu(
                isExpanded = isFabMenuOpen,
                onToggle = { isFabMenuOpen = !isFabMenuOpen },
                onQrClick = {
                    navController.navigate("qr_screen")
                    qrViewModel.fetchSeed()
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
                QrScreen(qrViewModel)
            }
            composable("scan_screen") {
                ScannerScreen(scannerViewModel)
            }
        }
    }
}

@Composable
fun MyAppTopBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val screenTitles = mapOf(
        "home" to "Home",
        "qr_screen" to "QR Code",
        "scan_screen" to "Scanner"
    )
    val title = screenTitles[currentRoute] ?: "App"

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (currentRoute != "home") {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "Go to Home")
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = title)
                }

                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    )
}


@Composable
fun PermissionDeniedScreen(onRequestPermission: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera permission is required to scan QR codes.", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
        }
    }
}
