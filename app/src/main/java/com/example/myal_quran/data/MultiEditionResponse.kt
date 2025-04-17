package com.example.myal_quran.data

data class EditionData(
    val edition: Edition,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val numberInSurah: Int,
    val text: String,
    val juz: Int,
    val surah: Surah
)


