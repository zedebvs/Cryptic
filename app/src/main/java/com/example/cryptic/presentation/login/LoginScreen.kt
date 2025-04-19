package com.example.cryptic.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cryptic.di.LocalAuthRepository
import com.example.cryptic.di.LocalMainViewModel
import com.example.cryptic.presentation.start.GradientBackground

@Composable
fun LoginScreen(navController: NavController) {
    val authRepository = LocalAuthRepository.current
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(authRepository)
    )

    GradientBackground {
        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        val state by viewModel.state.collectAsState()
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Log In",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 56.sp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(64.dp))

            CustomTextField(
                value = emailText,
                onValueChange = { emailText = it },
                placeholder = "Login"
            )

            CustomTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                placeholder = "Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot password?",
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White,
                    modifier = Modifier.clickable {
                        navController.navigate("reset_password")
                    },
                )
            }
            when (state) {
                is LoginViewModel.LoginState.Loading -> {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(16.dp))
                }
                is LoginViewModel.LoginState.Error -> {
                    Text(
                        text = (state as LoginViewModel.LoginState.Error).message,
                        color = Color.Red,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is LoginViewModel.LoginState.Success -> {
                    val mainViewModel = LocalMainViewModel.current
                    val userProfile = (state as LoginViewModel.LoginState.Success).profile

                    LaunchedEffect(Unit) {
                        mainViewModel.setProfile(userProfile)
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                else -> {}
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(emailText, passwordText)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F71DE),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(horizontal = 8.dp, vertical = 24.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(
                    text = "Log In",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Up",
                    modifier = Modifier.clickable {
                        navController.navigate("registration") {
                            popUpTo("start") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(fontSize = 30.sp),
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            color = Color.Gray,
                            style = TextStyle(fontSize = 24.sp)
                        )
                    }
                    innerTextField()
                }
                if (isPassword) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color.Gray,
                        modifier = Modifier
                            .clickable { isPasswordVisible = !isPasswordVisible }
                            .padding(start = 8.dp)
                    )
                }
            }
        }

    )
}

