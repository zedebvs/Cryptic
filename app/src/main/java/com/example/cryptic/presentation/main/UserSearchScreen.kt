//package com.example.cryptic.presentation.main
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.Done
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import coil.compose.AsyncImage
//import com.example.cryptic.data.local.UserData
//import com.example.cryptic.di.LocalMainViewModel//
//import com.example.cryptic.di.LocalProfileViewModel//
//
//@Composable
//fun UserSearchScreen(navController: NavHostController) {
//
//    val localSearchViewModel = LocalSearchViewModel.current
//    val searchQuery = remember { mutableStateOf("") }
//    //val searchResults by mainViewModel.searchResults.collectAsState()
//
//    GradientBackgroundHome {
//        Column(
//            modifier = Modifier
//                .systemBarsPadding()
//                .fillMaxSize()
//                .background(Color(0xFF202126))
//                .padding(16.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFF2F2F36), shape = CircleShape)
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = null,
//                    tint = Color.Gray,
//                    modifier = Modifier.size(24.dp)
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                androidx.compose.material3.TextField(
//                    value = searchQuery.value,
//                    onValueChange = {
//                        searchQuery.value = it
//                        //mainViewModel.searchUsers(it) // функция поиска
//                    },
//                    placeholder = { Text("Введите имя пользователя", color = Color.Gray) },
//                    singleLine = true,
//                    modifier = Modifier
//                        .weight(1f)
//                        .background(Color.Transparent),
//                    colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
//                        textColor = Color.White,
//                        containerColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            LazyColumn(
//                modifier = Modifier.fillMaxSize()
//            ) {
////                items(searchResults) { user ->
////                    UserSearchItem(user = user, onViewProfile = {
////                        navController.navigate("profile/${user.id}")
////                    }, onSendMessage = {
////                        navController.navigate("chat/${user.id}")
////                    })
////                    Spacer(modifier = Modifier.height(8.dp))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun UserSearchItem(
//    user: UserData, // твоя моделька с id, name, avatar, status
//    onViewProfile: () -> Unit,
//    onSendMessage: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFF2F2F36), shape = CircleShape)
//            .padding(12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        AsyncImage(
//            model = user.avatarUrl,
//            contentDescription = "Avatar",
//            modifier = Modifier
//                .size(54.dp)
//                .clip(CircleShape)
//        )
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column(
//            modifier = Modifier.weight(1f)
//        ) {
//            Text(text = user.name, color = Color.White, fontSize = 18.sp)
//            Text(
//                text = if (user.isOnline) "В сети" else "Не в сети",
//                color = if (user.isOnline) Color(0xFF4CAF50) else Color.Gray,
//                fontSize = 14.sp
//            )
//        }
//
//        IconButton(onClick = onViewProfile) {
//            Icon(Icons.Default.AccountCircle, contentDescription = "Профиль", tint = Color.Gray)
//        }
//
//        IconButton(onClick = onSendMessage) {
//            Icon(Icons.Default.Done, contentDescription = "Написать", tint = Color.Gray)
//        }
//    }
//}