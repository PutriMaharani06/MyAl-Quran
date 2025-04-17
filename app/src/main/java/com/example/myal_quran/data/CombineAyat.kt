package com.example.myal_quran.data

data class CombineAyat(
    val numberInSurah: Int,
    val arabText: String,
    val translation: String,
    val juz: Int,
    val audioUrl: String?
)
