package com.example.myal_quran.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myal_quran.data.CombineAyat
import com.example.myal_quran.data.RetrofitClient
import com.example.myal_quran.data.SurahDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _ayahList = MutableStateFlow<List<CombineAyat>>(emptyList())
    val ayahList: StateFlow<List<CombineAyat>> = _ayahList

    private val _filteredAyatList = MutableStateFlow<List<CombineAyat>>(emptyList())
    val filteredAyatList: StateFlow<List<CombineAyat>> = _filteredAyatList

    val searchQuery = MutableStateFlow("")

    val isLoading = MutableStateFlow(false)
    val surahDetail = MutableStateFlow<SurahDetail?>(null)

    private var mediaPlayer: MediaPlayer? = null

    val juzList: StateFlow<List<Int>> = _filteredAyatList
        .map { ayats -> ayats.map { it.juz }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    init {
        viewModelScope.launch {
            searchQuery.collect { query ->
                _filteredAyatList.value = if (query.isBlank()) {
                    _ayahList.value
                } else {
                    _ayahList.value.filter {
                        it.translation.contains(query, ignoreCase = true) ||
                                it.arabText.contains(query, ignoreCase = true)
                    }
                }
            }
        }
    }

    fun fetchAyatBySurah(surahId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val arab = RetrofitClient.apiService.getAyatArabBySurah(surahId)
                val indo = RetrofitClient.apiService.getAyatBySurah(surahId)
                val audio = RetrofitClient.apiService.getAudioBySurah(surahId)

                val combined = arab.data.ayahs.mapNotNull { arabAyat ->
                    val indoAyat = indo.data.ayahs.find { it.numberInSurah == arabAyat.numberInSurah }
                    val audioAyat = audio.data.ayahs.find { it.numberInSurah == arabAyat.numberInSurah }
                    indoAyat?.let {
                        CombineAyat(
                            numberInSurah = arabAyat.numberInSurah,
                            arabText = arabAyat.text,
                            translation = it.text,
                            juz = arabAyat.juz,
                            audioUrl = audioAyat?.audio
                        )
                    }
                }
                _ayahList.value = combined
                _filteredAyatList.value = combined
                surahDetail.value = arab.data

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun playAudio(url: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepare()
            start()
        }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
