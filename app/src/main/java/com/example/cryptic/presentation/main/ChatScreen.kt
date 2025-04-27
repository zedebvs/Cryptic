//package com.example.cryptic.presentation.main
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cryptic.di.LocalChatViewModel
import com.example.cryptic.presentation.main.GradientBackgroundHome
import com.example.cryptic.presentation.start.GradientBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


@Composable
fun ChatScreen(recipientId: Int) {
    val chatViewModel = LocalChatViewModel.current
    val messages by chatViewModel.messages.collectAsState()
    val profile by chatViewModel.profile.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val headerColor = Color(0xFF202126)
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(isKeyboardVisible, messages.size) {
        if (isKeyboardVisible && messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
    LaunchedEffect(recipientId) {
        chatViewModel.requestMessages(recipientId)
    }


    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = headerColor,
            darkIcons = false
        )
    }
    Scaffold(
        topBar = {
            profile?.let { user ->
                ChatHeader(
                    name = user.name,
                    avatarUrl = user.avatar,
                    status = if (user.online > 0) "online" else formatLastonline(user.lastonline)

                )

            }
        },
        containerColor = Color(0xFF1E1E24)
    )
    { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    state = listState,
                    reverseLayout = false,
                    verticalArrangement = Arrangement.Bottom,
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(messages) { message ->
                        MessageBubble(
                            text = message.text,
                            isMine = message.sender_id != profile?.profile_id,
                            time = message.timestamp,
                            status = message.status,
                            isEdited = message.is_edited
                        )
                    }
                }

                MessageInputField(
                    onMessageSent = { text ->
                        chatViewModel.sendMessage(recipientId, text)
                    }
                )
            }
        }
    }
}

@Composable
fun ChatHeader(
    name: String,
    avatarUrl: String,
    status: String
) {
    Surface(
        color = Color(0xFF2A2A32),
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар
            Box(modifier = Modifier.size(50.dp)) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )

                // Индикатор онлайн статуса
                if (status == "online") {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                            .border(2.dp, Color(0xFF2A2A32), CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Имя и статус
            Column {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = status,
                    color = Color(0xFFA0A0A0),
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Дополнительные кнопки
            IconButton(onClick = { /* Действия */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    text: String,
    isMine: Boolean,
    time: String,
    status: String,
    isEdited: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 4.dp),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = when {
                isMine -> RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp)
                else -> RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)
            },
            color = if (isMine) Color(0xFF6C5CE7) else Color(0xFF2A2A32),
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isEdited) {
                        Text(
                            text = "ред.",
                            color = Color(0xFFA0A0A0),
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = formatMessageTime(time),
                        color = Color(0xFFA0A0A0),
                        fontSize = 10.sp
                    )

                    if (isMine) {
                        Spacer(modifier = Modifier.width(4.dp))
                        val (icon, tint) = when (status) {
                            "sent" -> Icons.Default.Done to Color(0xFFA0A0A0)
                            "delivered" -> Icons.Default.DoneAll to Color(0xFFA0A0A0)
                            "read" -> Icons.Default.DoneAll to Color(0xFF4CAF50)
                            else -> Icons.Default.Schedule to Color(0xFFA0A0A0)
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = "Status",
                            tint = tint,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageInputField(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

        Surface(
            color = Color(0xFF2A2A32),
            modifier = modifier
                .fillMaxWidth()
        ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка прикрепления файла
            IconButton(
                onClick = { /* Прикрепить файл */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach",
                    tint = Color(0xFFA0A0A0),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Поле ввода
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp, max = 120.dp)
                    .background(Color(0xFF1E1E24), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    ),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = "Напишите сообщение...",
                                color = Color(0xFFA0A0A0),
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    },
                    singleLine = false,
                    maxLines = 5
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка отправки
            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onMessageSent(text)
                        text = ""
                    }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color(0xFF6C5CE7),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

fun formatMessageTime(timestamp: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        LocalDateTime.parse(timestamp).format(formatter)
    } catch (e: Exception) {
        ""
    }
}

fun formatLastonline(lastonline: String?): String {
    if (lastonline.isNullOrEmpty()) return "offline"

    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val dateTime = LocalDateTime.parse(lastonline, formatter)

        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val date = dateTime.toLocalDate()
        val daysAgo = ChronoUnit.DAYS.between(date, today)

        when (daysAgo) {
            0L -> "был(а) сегодня в ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            1L -> "был(а) вчера в ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            2L -> "был(а) позавчера в ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            else -> "был(а) ${dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
        }
    } catch (e: Exception) {
        "offline"
    }
}



