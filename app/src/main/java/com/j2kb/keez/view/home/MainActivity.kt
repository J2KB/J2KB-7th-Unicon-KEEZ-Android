package com.j2kb.keez.view.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.j2kb.keez.ui.theme.KEEZTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, intent.getStringExtra("token"), Toast.LENGTH_SHORT).show()

        setContent {
            KEEZTheme {
                MainScreenView()
            }
        }
    }

    @Composable
    fun MainScreenView() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController = navController) }
        ) {
            Box(Modifier.padding(it)){
                NavigationGraph(navController = navController)
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.Calendar,
            BottomNavItem.Timeline,
            BottomNavItem.Settings
        )

        BottomNavigation(
            backgroundColor = Color.White,
            contentColor = Color(0xFF3F414E)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)
                        )
                    },
                    label = { Text(item.title, fontSize = 9.sp) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Gray,
                    selected = currentRoute == item.screenRoute,
                    alwaysShowLabel = false,
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    sealed class BottomNavItem(
        val title: String, val icon: ImageVector, val screenRoute: String
    ) {

        object Calendar : BottomNavItem("홈", Icons.Default.Home, "Home")
        object Timeline : BottomNavItem("랭킹", Icons.Default.Favorite, "Ranking")
        object Settings : BottomNavItem("마이페이지", Icons.Default.AccountCircle, "MyPage")
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = BottomNavItem.Calendar.screenRoute) {
            composable(BottomNavItem.Calendar.screenRoute) {
                CalendarScreen()
            }
            composable(BottomNavItem.Timeline.screenRoute) {
                TimelineScreen()
            }
            composable(BottomNavItem.Settings.screenRoute) {
                SettingsScreen()
            }
        }
    }


    @Composable
    fun CalendarScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            Text(
                text = "홈",
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Composable
    fun TimelineScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            Text(
                text = "랭킹",
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Composable
    fun SettingsScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondaryVariant)
        ) {
            Text(
                text = "마이페이지",
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
