package com.example.myal_quran.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myal_quran.viewmodel.DetailViewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSurahScreen(surahId: Int, viewModel: DetailViewModel, navController: NavController) {
    val ayatList = viewModel.filteredAyatList.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val surahDetail = viewModel.surahDetail.collectAsState().value
    val searchText = remember { mutableStateOf("") }
    val juzList = viewModel.juzList.collectAsState().value


    val primaryColor = Color(0xFF6D9886)
    val secondaryColor = Color(0xFFDEF5E5)
    val arabicTextColor = Color(0xFF222831)
    val translationTextColor = Color(0xFF393E46)
    val backgroundColor = Color(0xFFF9F9F9)

    LaunchedEffect(surahId) {
        viewModel.fetchAyatBySurah(surahId)
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail Surah", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                    viewModel.searchQuery.value = it
                },
                label = { Text("Cari Ayat...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    surahDetail?.let { detail ->
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = primaryColor),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${detail.name} - ${detail.englishName}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Terjemahan: ${detail.englishNameTranslation}",
                                        color = Color.White
                                    )
                                    Text(
                                        "Jumlah Ayat: ${detail.numberOfAyahs}",
                                        color = Color.White
                                    )
                                    Text(
                                        "Tipe Wahyu: ${detail.revelationType}",
                                        color = Color.White
                                    )
                                    Text("Juz: ${juzList.joinToString()}", color = Color.White)

                                }
                            }
                        }
                    }

                    items(ayatList) { ayat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = secondaryColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                // Ayat Arab
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = ayat.arabText,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = arabicTextColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Nomor Ayat dalam lingkaran
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = primaryColor,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = ayat.numberInSurah.toString(),
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Terjemahan
                                Text(
                                    text = ayat.translation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = translationTextColor
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            ayat.audioUrl?.let { viewModel.playAudio(it) }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                                    ) {
                                        Text("Putar")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            viewModel.stopAudio()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                                    ) {
                                        Text("Stop")
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