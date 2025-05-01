package com.example.myal_quran.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myal_quran.viewmodel.SurahViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    navController: NavController,
    viewModel: SurahViewModel,
    account: GoogleSignInAccount?,
    onLogoutClicked: () -> Unit
) {
    val surahList = viewModel.surahList.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val backgroundColor = Color(0xFFF9F5F0)
    val cardColor = Color(0xFFFFFFFF)
    val accentColor = Color(0xFF6D9886)
    val textColor = Color(0xFF393E46)

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Daftar Surah", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = accentColor)
            )
        }
    ) { padding ->

        if (showProfileDialog && account != null) {
            AlertDialog(
                onDismissRequest = { showProfileDialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showProfileDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Tutup")
                        }
                        Button(
                            onClick = {
                                onLogoutClicked()
                                showProfileDialog = false
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("Logout", color = Color.White)
                        }
                    }
                },
                title = { Text("Profil Pengguna", fontWeight = FontWeight.Bold) },
                text = {
                    Column(horizontalAlignment = Alignment.Start) {
                        account.photoUrl?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Foto Profil",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(MaterialTheme.shapes.medium)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        Text("Nama: ${account.displayName ?: "-"}")
                        Text("Email: ${account.email ?: "-"}")
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            )
        }

        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {

                    if (account != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showProfileDialog = true }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            account.photoUrl?.let { imageUrl ->
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Foto Profil",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(end = 8.dp)
                                        .clip(MaterialTheme.shapes.small)
                                )
                            }
                            Column {
                                Text(
                                    text = account.displayName ?: "Pengguna",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = account.email ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Cari Surah") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            cursorColor = accentColor
                        )
                    )

                    val filteredList = if (searchQuery.text.isEmpty()) {
                        surahList
                    } else {
                        surahList.filter {
                            it.englishName.contains(searchQuery.text, ignoreCase = true)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(filteredList) { surah ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        navController.navigate("detail/${surah.number}")
                                    },
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(accentColor, shape = MaterialTheme.shapes.small),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = surah.number.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = surah.englishName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = surah.englishNameTranslation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
