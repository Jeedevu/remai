package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.RemRepository
import com.example.ui.components.HardShadowCard
import com.example.ui.screens.*
import com.example.ui.theme.*

enum class AppScreen {
    BOOT,
    ONBOARDING,
    AUTH,
    HOME,
    MEMORY,
    QUICK_CAPTURE,
    CAMERA_SCAN,
    CALENDAR,
    PROFILE,
    FOCUS_MODE,
    ASSISTANT
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: RemRepository
    private var sharedContentState = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = RemRepository.getInstance(applicationContext)

        handleIncomingIntent(intent)

        setContent {
            MyApplicationTheme {
                MainAppContainer(
                    repository = repository,
                    initialSharedContent = sharedContentState.value
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_SEND -> {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val stream = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                if (!text.isNullOrBlank()) {
                    sharedContentState.value = text
                } else if (stream != null) {
                    sharedContentState.value = "Shared Attachment: ${stream.lastPathSegment ?: "document"}"
                }
            }
        }
    }
}

@Composable
fun MainAppContainer(
    repository: RemRepository,
    initialSharedContent: String? = null
) {
    var currentScreen by remember { mutableStateOf(if (initialSharedContent != null) AppScreen.QUICK_CAPTURE else AppScreen.BOOT) }
    var previousScreen by remember { mutableStateOf(AppScreen.HOME) }

    fun navigateTo(screen: AppScreen) {
        previousScreen = currentScreen
        currentScreen = screen
    }

    // Hardware Back Button handling
    BackHandler(enabled = currentScreen != AppScreen.HOME && currentScreen != AppScreen.BOOT) {
        when (currentScreen) {
            AppScreen.FOCUS_MODE, AppScreen.ASSISTANT, AppScreen.QUICK_CAPTURE, AppScreen.CAMERA_SCAN -> {
                currentScreen = previousScreen
            }
            AppScreen.MEMORY, AppScreen.CALENDAR, AppScreen.PROFILE -> {
                currentScreen = AppScreen.HOME
            }
            AppScreen.ONBOARDING -> {
                currentScreen = AppScreen.BOOT
            }
            AppScreen.AUTH -> {
                currentScreen = AppScreen.ONBOARDING
            }
            else -> {
                currentScreen = AppScreen.HOME
            }
        }
    }

    val isMainTab = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.MEMORY,
        AppScreen.CALENDAR,
        AppScreen.PROFILE
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = RemSurface,
        bottomBar = {
            if (isMainTab) {
                RemBottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isMainTab) PaddingValues(bottom = innerPadding.calculateBottomPadding()) else PaddingValues(0.dp))
        ) {
            when (currentScreen) {
                AppScreen.BOOT -> {
                    BootScreen(
                        onBootDashboard = { navigateTo(AppScreen.HOME) },
                        onSkipToOnboarding = { navigateTo(AppScreen.ONBOARDING) }
                    )
                }
                AppScreen.ONBOARDING -> {
                    OnboardingScreen(
                        onGetStarted = { navigateTo(AppScreen.HOME) },
                        onSignIn = { navigateTo(AppScreen.AUTH) }
                    )
                }
                AppScreen.AUTH -> {
                    AuthScreen(
                        onNavigateBack = { navigateTo(AppScreen.ONBOARDING) },
                        onEnterVault = { navigateTo(AppScreen.HOME) },
                        onCreateAccount = { navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.HOME -> {
                    HomeScreen(
                        repository = repository,
                        onNavigateToQuickCapture = { navigateTo(AppScreen.QUICK_CAPTURE) },
                        onNavigateToFocusMode = { navigateTo(AppScreen.FOCUS_MODE) },
                        onNavigateToCalendar = { navigateTo(AppScreen.CALENDAR) },
                        onNavigateToProfile = { navigateTo(AppScreen.PROFILE) },
                        onNavigateToAssistant = { navigateTo(AppScreen.ASSISTANT) }
                    )
                }
                AppScreen.MEMORY -> {
                    MemoryVaultScreen(
                        repository = repository,
                        onNavigateToQuickCapture = { navigateTo(AppScreen.QUICK_CAPTURE) },
                        onNavigateToAssistant = { navigateTo(AppScreen.ASSISTANT) },
                        onNavigateToProfile = { navigateTo(AppScreen.PROFILE) }
                    )
                }
                AppScreen.QUICK_CAPTURE -> {
                    QuickCaptureScreen(
                        repository = repository,
                        onNavigateBack = { navigateTo(previousScreen) },
                        onCaptureSaved = { navigateTo(AppScreen.MEMORY) },
                        onOpenScanner = { navigateTo(AppScreen.CAMERA_SCAN) }
                    )
                }
                AppScreen.CAMERA_SCAN -> {
                    CameraCaptureScreen(
                        repository = repository,
                        onNavigateBack = { navigateTo(previousScreen) },
                        onCaptured = { navigateTo(AppScreen.MEMORY) }
                    )
                }
                AppScreen.CALENDAR -> {
                    CalendarScreen(
                        repository = repository,
                        onNavigateBack = { navigateTo(AppScreen.HOME) },
                        onStartFocusSprint = { navigateTo(AppScreen.FOCUS_MODE) }
                    )
                }
                AppScreen.PROFILE -> {
                    ProfileScreen(
                        repository = repository,
                        onNavigateBack = { navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.FOCUS_MODE -> {
                    FocusModeScreen(
                        onExit = { navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.ASSISTANT -> {
                    AssistantScreen(
                        repository = repository,
                        onNavigateBack = { navigateTo(previousScreen) }
                    )
                }
            }
        }
    }
}

@Composable
fun RemBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(RemSurface.copy(alpha = 0.95f))
            .border(width = 1.dp, color = RemInkBlack.copy(alpha = 0.15f))
            .navigationBarsPadding()
            .height(72.dp)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            NavTabItem(
                label = "Home",
                icon = Icons.Default.Home,
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onNavigate(AppScreen.HOME) }
            )

            // Memory
            NavTabItem(
                label = "Memory",
                icon = Icons.Default.Inventory2,
                isSelected = currentScreen == AppScreen.MEMORY,
                onClick = { onNavigate(AppScreen.MEMORY) }
            )

            // Elevated Center Button: + REMEMBER
            Box(
                modifier = Modifier
                    .offset(y = (-14).dp)
                    .clickable { onNavigate(AppScreen.QUICK_CAPTURE) }
            ) {
                HardShadowCard(
                    backgroundColor = RemPrimaryContainer,
                    shadowOffset = 3.dp,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Remember",
                            tint = RemInkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "REMEMBER",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = RemInkBlack,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Calendar
            NavTabItem(
                label = "Calendar",
                icon = Icons.Default.CalendarToday,
                isSelected = currentScreen == AppScreen.CALENDAR,
                onClick = { onNavigate(AppScreen.CALENDAR) }
            )

            // Profile
            NavTabItem(
                label = "Profile",
                icon = Icons.Default.SentimentSatisfied,
                isSelected = currentScreen == AppScreen.PROFILE,
                onClick = { onNavigate(AppScreen.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) RemOnSurface else RemOnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            color = if (isSelected) RemOnSurface else RemOnSurfaceVariant,
            letterSpacing = 0.5.sp
        )
    }
}
