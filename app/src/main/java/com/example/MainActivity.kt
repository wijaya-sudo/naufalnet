package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Screen
import com.example.ui.screens.AdminTokenScreen
import com.example.ui.screens.ExamInstructionScreen
import com.example.ui.screens.ExamRoomScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.ReviewScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CbtViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: CbtViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    val screenModifier = Modifier.padding(innerPadding)
                    when (val screen = currentScreen) {
                        is Screen.Home -> {
                            HomeScreen(viewModel = viewModel, modifier = screenModifier)
                        }
                        is Screen.Instruction -> {
                            ExamInstructionScreen(viewModel = viewModel, modifier = screenModifier)
                        }
                        is Screen.ExamRoom -> {
                            ExamRoomScreen(viewModel = viewModel, modifier = screenModifier)
                        }
                        is Screen.Result -> {
                            ResultScreen(
                                resultId = screen.resultId,
                                viewModel = viewModel,
                                modifier = screenModifier
                            )
                        }
                        is Screen.Review -> {
                            ReviewScreen(
                                packageId = screen.packageId,
                                resultId = screen.resultId,
                                viewModel = viewModel,
                                modifier = screenModifier
                            )
                        }
                        is Screen.History -> {
                            HistoryScreen(viewModel = viewModel, modifier = screenModifier)
                        }
                        is Screen.AdminToken -> {
                            AdminTokenScreen(viewModel = viewModel, modifier = screenModifier)
                        }
                    }
                }
            }
        }
    }
}
