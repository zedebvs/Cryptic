package com.example.cryptic.presentation.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.cryptic.di.LocalProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import androidx.compose.ui.platform.LocalContext

//import com.example.cryptic.presentation.login.User


@Composable
fun ProfileScreen(navController: NavHostController) {

    val profileViewModel = LocalProfileViewModel.current
    val privateProfile by profileViewModel.profile.collectAsState()
    var aboutText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val imageUrl = privateProfile?.avatar
    val context = LocalContext.current
    val online = privateProfile?.online ?: 0

    LaunchedEffect(privateProfile) {
        privateProfile?.status?.let {
            aboutText = it
        }
    }
    LaunchedEffect(Unit) {
        profileViewModel.fetchProfile()
    }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {

            val inputStream = context.contentResolver.openInputStream(it)
            val tempFile = File(context.cacheDir, "avatar_temp.jpg")
            tempFile.outputStream().use { fileOut ->
                inputStream?.copyTo(fileOut)
            }

            val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

            profileViewModel.uploadAvatar(body)
        }}
    GradientBackgroundHome() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF202126))
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()

                )
//                Image(
//                    painter = painterResource(id = com.example.cryptic.R.drawable.test_image),
//                    contentDescription = "Аватар",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(CircleShape)
//                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (online > 0) {
                Text("🟢 В сети", color = Color(0xFF4CAF50), fontSize = 18.sp)
            } else {
                Text("🔴 Не в сети", color = Color.Red, fontSize = 18.sp)

            }
                Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2F2F36), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    //Text("Имя: ${test_user.login}", color = Color.White, fontSize = 24.sp)
                    Text("Имя: ${privateProfile?.name ?: "Загрузка..."}", color = Color.White, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Почта: ${privateProfile?.email ?: "Загрузка..."}", color = Color.LightGray, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            pickImageLauncher.launch("image/*")
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Изменить аватар")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2F2F36), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
            ) {
                Column {
                    Text("О себе", color = Color.White, fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    BasicTextField(
                        value = aboutText,
                        onValueChange = { aboutText = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3A3A45), shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            profileViewModel.updateStatus(aboutText)
                            keyboardController?.hide()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Сохранить")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    profileViewModel.logout()
                    navController.navigate("start") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB42424)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Выйти из аккаунта", fontSize = 20.sp)
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ProfileScreen1() {
//    val navController = rememberNavController()
//    ProfileScreen(navController = navController, viewModelviewModel)
//}