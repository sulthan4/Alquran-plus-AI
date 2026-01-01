package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.models.TafsirText
import com.alquranplusai.domain.models.AyahTranslation
import com.alquranplusai.domain.models.Tafsir
import com.alquranplusai.domain.repositories.QuranRepository
import com.alquranplusai.domain.repositories.TranslationRepository
import com.alquranplusai.domain.repositories.TafsirRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AyahDetailUiState(
    val ayah: Ayah? = null,
    val translations: List<AyahTranslation> = emptyList(),
    val tafsir: TafsirText? = null,
    val availableTafsirs: List<Tafsir> = emptyList(),
    val selectedTafsirId: String? = null,
    val isLoading: Boolean = false
)

class AyahDetailViewModel(
    private val quranRepository: QuranRepository,
    private val translationRepository: TranslationRepository,
    private val tafsirRepository: TafsirRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AyahDetailUiState())
    val uiState: StateFlow<AyahDetailUiState> = _uiState.asStateFlow()

    fun loadAyah(surahNumber: Int, ayahNumber: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Launch parallel loading
            launch {
                quranRepository.getAyahByNumber(surahNumber, ayahNumber).collectLatest { ayah ->
                    _uiState.update { it.copy(ayah = ayah) }
                }
            }
            
            // Load available tafsirs
            launch {
                tafsirRepository.getDownloadedTafsirs().collectLatest { tafsirs ->
                    _uiState.update { it.copy(availableTafsirs = tafsirs) }
                    // Select first if none selected
                    if (tafsirs.isNotEmpty() && _uiState.value.selectedTafsirId == null) {
                        loadTafsir(surahNumber, ayahNumber, tafsirs.first().id)
                    }
                }
            }

            // Load translations (using hardcoded IDs or user settings)
            // TODO: Use SettingsManager to get selected translation IDs
            launch {
                translationRepository.getAyahTranslations(surahNumber, ayahNumber, listOf("en_sahih")).collect { trans ->
                    _uiState.update { it.copy(translations = trans) }
                }
            }
            
             _uiState.update { it.copy(isLoading = false) }
        }
    }
    
    fun loadTafsir(surahNumber: Int, ayahNumber: Int, tafsirId: String) {
        viewModelScope.launch {
             _uiState.update { it.copy(selectedTafsirId = tafsirId, isLoading = true) }
             tafsirRepository.getTafsirForAyah(tafsirId, surahNumber, ayahNumber).collect { tafsir ->
                 _uiState.update { it.copy(tafsir = tafsir, isLoading = false) }
             }
        }
    }
}
