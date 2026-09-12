package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.BibleViewModel
import com.example.ui.NavTab
import com.example.ui.components.AudioMiniPlayer
import com.example.ui.screens.AiHubScreen
import com.example.ui.screens.BibleReaderScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.StudyHubScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: BibleViewModel = viewModel()
                BibliaVivaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BibliaVivaApp(viewModel: BibleViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("biblia_viva_scaffold"),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Column {
                // Persistent mini player when audio is active
                AudioMiniPlayer(audioPlayer = viewModel.audioPlayer)

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == NavTab.HOME,
                        onClick = { viewModel.setTab(NavTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Início"
                            )
                        },
                        label = { Text("Início", fontSize = 11.sp, fontWeight = if (currentTab == NavTab.HOME) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NavyPrimary,
                            selectedTextColor = NavyPrimary,
                            indicatorColor = Color(0xFFDBEAFE),
                            unselectedIconColor = SubtitleText,
                            unselectedTextColor = SubtitleText
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavTab.BIBLE,
                        onClick = { viewModel.setTab(NavTab.BIBLE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavTab.BIBLE) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                contentDescription = "Bíblia"
                            )
                        },
                        label = { Text("Bíblia", fontSize = 11.sp, fontWeight = if (currentTab == NavTab.BIBLE) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VividEmerald,
                            selectedTextColor = VividEmerald,
                            indicatorColor = Color(0xFFD1FAE5),
                            unselectedIconColor = SubtitleText,
                            unselectedTextColor = SubtitleText
                        ),
                        modifier = Modifier.testTag("nav_item_bible")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavTab.STUDY,
                        onClick = { viewModel.setTab(NavTab.STUDY) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavTab.STUDY) Icons.Filled.School else Icons.Outlined.School,
                                contentDescription = "Estudo"
                            )
                        },
                        label = { Text("Estudo", fontSize = 11.sp, fontWeight = if (currentTab == NavTab.STUDY) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VividIndigo,
                            selectedTextColor = VividIndigo,
                            indicatorColor = Color(0xFFE0E7FF),
                            unselectedIconColor = SubtitleText,
                            unselectedTextColor = SubtitleText
                        ),
                        modifier = Modifier.testTag("nav_item_study")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavTab.AI_TOOLS,
                        onClick = { viewModel.setTab(NavTab.AI_TOOLS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavTab.AI_TOOLS) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                contentDescription = "IA"
                            )
                        },
                        label = { Text("IA", fontSize = 11.sp, fontWeight = if (currentTab == NavTab.AI_TOOLS) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldAccent,
                            selectedTextColor = GoldAccent,
                            indicatorColor = Color(0xFFFEF3C7),
                            unselectedIconColor = SubtitleText,
                            unselectedTextColor = SubtitleText
                        ),
                        modifier = Modifier.testTag("nav_item_ai")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavTab.PROFILE,
                        onClick = { viewModel.setTab(NavTab.PROFILE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Perfil"
                            )
                        },
                        label = { Text("Perfil", fontSize = 11.sp, fontWeight = if (currentTab == NavTab.PROFILE) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VividRose,
                            selectedTextColor = VividRose,
                            indicatorColor = Color(0xFFFFE4E6),
                            unselectedIconColor = SubtitleText,
                            unselectedTextColor = SubtitleText
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavTab.HOME -> HomeScreen(viewModel = viewModel)
                NavTab.BIBLE -> BibleReaderScreen(viewModel = viewModel)
                NavTab.STUDY -> StudyHubScreen(viewModel = viewModel)
                NavTab.AI_TOOLS -> AiHubScreen(viewModel = viewModel)
                NavTab.PROFILE -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}

// Backward compatibility for unit / screenshot tests
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Bíblia Viva") }
}
