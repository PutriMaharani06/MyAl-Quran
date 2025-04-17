package com.example.myal_quran.data


data class SurahDetail(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int,
    val ayahs: List<Ayat>,
    val edition: Edition
)
