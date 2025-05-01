package com.example.alquran

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.myal_quran.auth.GoogleAuthUiClient
import com.example.myal_quran.navigasi.QuranNavGraph
import com.example.myal_quran.ui.theme.MyAlQuranTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyAlQuranTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val googleAuthUiClient = remember { GoogleAuthUiClient(context) }

                var user by remember { mutableStateOf<GoogleSignInAccount?>(null) }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    googleAuthUiClient.signInWithIntent(result.data) { success, firebaseUser ->
                        if (success) {
                            user = GoogleSignIn.getLastSignedInAccount(context)
                            Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            navController.navigate("surah_list") {
                                popUpTo("home") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Login Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                QuranNavGraph(
                    navController = navController,
                    account = user,
                    onLoginClicked = {
                        launcher.launch(googleAuthUiClient.getSignInIntent())
                    },
                    onLogoutClicked = {
                        googleAuthUiClient.signOut {
                            user = null
                            Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}






