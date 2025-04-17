package com.example.myal_quran.data

data class Ayat(
    val number: Int,
    val text: String = "",
    val numberInSurah: Int,
    val juz: Int,
    val audio: String?,
)