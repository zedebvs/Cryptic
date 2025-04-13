package com.example.cryptic.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

data class SettingOption(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {

    val profileSettings = listOf(
        SettingOption("Сменить никнейм", Icons.Default.Edit) {

        },
        SettingOption("Сменить почту", Icons.Default.Email) {

        },
        SettingOption("Изменить пароль", Icons.Default.Lock) {

        },
        SettingOption("Изменить аватар", Icons.Default.Person) {

        }
//        SettingOption("Задать статус", Icons.Default.PersonPin) {
//
//        }
    )

    val securitySettings = listOf(
        SettingOption("Защита чатов", Icons.Default.Security) {

        },
        SettingOption("Показать ключи чатов", Icons.Default.VpnKey) {

        }
    )

    val dataSettings = listOf(
        SettingOption("Очистка памяти", Icons.Default.DeleteSweep) {

        },
        SettingOption("Экспорт бэкапа", Icons.Default.Backup) {

        }
    )

    val appSettings = listOf(
        SettingOption("Темная тема", Icons.Outlined.Brightness4) {

        },
        SettingOption("Уведомления", Icons.Default.Notifications) {

        },
        SettingOption("О приложении", Icons.Default.Info) {

        },
        SettingOption("Удалить аккаунт", Icons.Default.Delete) {

        }
    )

    Scaffold(
        containerColor = Color(0xFF202126),
        topBar = {
            TopAppBar(
                title = { Text("Настройки", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2F2F36)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF202126))
                .padding(16.dp)
        ) {
            item {
                SettingSection("Профиль", profileSettings)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingSection("Безопасность", securitySettings)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingSection("Данные", dataSettings)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingSection("Приложение", appSettings)
            }
        }
    }
}

@Composable
fun SettingSection(title: String, options: List<SettingOption>) {
    Column {
        Text(
            text = title,
            color = Color.LightGray,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        options.forEach {
            SettingItem(it)
        }
    }
}

@Composable
fun SettingItem(option: SettingOption) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { option.onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = option.title,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = option.title,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    val navController = rememberNavController()
    SettingsScreen(navController)
}