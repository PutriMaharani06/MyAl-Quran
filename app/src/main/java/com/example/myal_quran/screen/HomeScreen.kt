package com.example.myal_quran.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alquran.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    account: GoogleSignInAccount?,
    onLoginClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {

    val primaryColor = Color(0xFF6D9886)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F6EF), Color(0xFFDEF5E5))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Al-Qur'an",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(gradientBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Assalamu’alaikum",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Selamat datang di aplikasi Al-Qur'an digital.\nMulai hari ini dengan membaca ayat-ayat suci!",
                fontSize = 16.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(id = R.drawable.alquran),
                contentDescription = "Qur'an Illustration",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )

            if (account == null) {
                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(text = "Login dengan Google")
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "👤 Profil Pengguna",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = primaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Nama: ${account.displayName ?: "-"}")
                        Text(text = "Email: ${account.email ?: "-"}")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = onLogoutClicked,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "Logout")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("surah_list") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📖 Buka Daftar Surah")
                }
            }
        }
    }
}
