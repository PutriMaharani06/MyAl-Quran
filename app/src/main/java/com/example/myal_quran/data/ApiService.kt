package com.example.myal_quran.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Mendapatkan semua Surah
    @GET("surah")
    suspend fun getAllSurah(): SurahResponse

    @GET("surah/{surahId}")
    suspend fun getAyatArabBySurah(
        @Path("surahId") surahId: Int
    ): AyatResponse


    // Mendapatkan ayat dari satu surah, dengan terjemahan Indonesia
    @GET("surah/{surahId}/id.indonesian")
    suspend fun getAyatBySurah(
        @Path("surahId") surahId: Int
    ): AyatResponse

    @GET("surah/{surahId}/ar.alafasy")
    suspend fun getAudioBySurah(
        @Path("surahId") surahId: Int
    ): AyatResponse


}


