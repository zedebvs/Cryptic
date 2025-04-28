package com.example.cryptic.presentation.register

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.cryptic.di.LocalRegistrationRepository
import com.example.cryptic.presentation.start.GradientBackground
import com.example.cryptic.ui.registration.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    val registerRepository = LocalRegistrationRepository.current
    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(registerRepository)
    )

        var emailText by remember { mutableStateOf("") }
        var nameText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        var repeatPasswordText by remember { mutableStateOf("") }
        val state by viewModel.state.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current
        val scrollState = rememberScrollState()

    LaunchedEffect(state) {
        if (state is RegisterViewModel.RegistrationState.Success) {
            navController.navigate("login") {
                popUpTo("registration") { inclusive = true }
            }
        }
    }

    GradientBackground() {
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign Up",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 56.sp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(64.dp))

            CustomTextField(
                value = emailText,
                onValueChange = { emailText = it },
                placeholder = "Email"
            )

            CustomTextField(
                value = nameText,
                onValueChange = { nameText = it },
                placeholder = "Name"
            )

            CustomTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                placeholder = "Password",
                isPassword = true
            )

            CustomTextField(
                value = repeatPasswordText,
                onValueChange = { repeatPasswordText = it },
                placeholder = "Repeat password",
                isPassword = true
            )

            if (state is RegisterViewModel.RegistrationState.Error) {
                val error = (state as RegisterViewModel.RegistrationState.Error).message
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.register(
                        email = emailText,
                        name = nameText,
                        password = passwordText,
                        repeatPassword = repeatPasswordText
                    )
                },
                enabled = state !is RegisterViewModel.RegistrationState.Loading,
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
                if (state is RegisterViewModel.RegistrationState.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        text = "Sign Up",
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign In",
                    modifier = Modifier.clickable {
                        navController.navigate("login") {
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

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    RegisterScreen(navController = navController)
}

