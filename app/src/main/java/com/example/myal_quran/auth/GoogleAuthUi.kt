package com.example.myal_quran.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alquran.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthUiClient(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    fun signOut(onComplete: () -> Unit) {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                onComplete()
            }
        }
    }
    fun signInWithIntent(
        intent: Intent?,
        onResult: (Boolean, String?) -> Unit
    ) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.result
            Log.d("SignIn", "Google account retrieved: ${account.email}")
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, auth.currentUser?.displayName)
                    } else {
                        Log.e("SignIn", "Firebase auth failed", task.exception)
                        onResult(false, null)
                    }
                }
        } catch (e: ApiException) {
            Log.e("SignIn", "Google Sign-In failed", e)
            onResult(false, null)
        }
    }
}