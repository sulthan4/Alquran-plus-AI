package com.alquranplusai.android.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alquranplusai.android.ui.viewmodels.ReadingViewModel
import com.alquranplusai.android.ui.viewmodels.AudioPlayerViewModel
import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.models.TafsirText
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    surahNumber: Int,
    ayahNumber: Int = 1,
    onNavigateBack: () -> Unit = {},
    onNavigateToTafsirSelection: () -> Unit = {},
    onNavigateToAudioPlayer: () -> Unit = {},
    viewModel: ReadingViewModel = koinViewModel(),
    audioViewModel: AudioPlayerViewModel = koinViewModel(),
    tafsirViewModel: com.alquranplusai.android.ui.viewmodels.TafsirViewModel = koinViewModel()
) {
    val ayahs by viewModel.ayahs.collectAsState()
    val isPlaying by audioViewModel.isPlaying.collectAsState()
    val activeWordPosition by audioViewModel.activeWordPosition.collectAsState()
    val activeAyahNumber by audioViewModel.activeAyahNumber.collectAsState()
    val activeSurahNumber by audioViewModel.activeSurahNumber.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val bookmarksMap by viewModel.bookmarksMap.collectAsState()
    val translationsMap by viewModel.translationsMap.collectAsState()
    
    // New State Observations
    val readingMode by viewModel.readingMode.collectAsState()
    val isWordByWordEnabled by viewModel.isWordByWordEnabled.collectAsState()
    
    // Reciter selection bottom sheet
    var showReciterSheet by remember { mutableStateOf(false) }
    
    val listState = rememberLazyListState()

    LaunchedEffect(surahNumber) {
        viewModel.loadAyahs(surahNumber)
    }

    LaunchedEffect(ayahs) {
        if (ayahs.isNotEmpty()) {
            audioViewModel.updatePlaybackContext(ayahs.first())
        }
    }
    
    // Load tafsir for all ayahs in this surah
    val allTafsirTexts by tafsirViewModel.currentTafsirTexts.collectAsState()
    val tafsirByAyah = remember(allTafsirTexts) {
        allTafsirTexts.groupBy { it.ayahNumber }
    }
    
    LaunchedEffect(surahNumber, ayahs) {
        if (ayahs.isNotEmpty()) {
            // Load tafsir for the entire surah
            tafsirViewModel.loadTafsirForSurah(surahNumber)
        }
    }

    // Auto-follow scrolling logic (Only for Continuous Mode currently, or manage Page scrolling)
    LaunchedEffect(activeAyahNumber) {
        if (readingMode == com.alquranplusai.domain.models.ReadingMode.CONTINUOUS) {
            println("AlQuranPlusAI: Screen - Active Ayah changed to $activeAyahNumber")
            activeAyahNumber?.let { ayahNum ->
                val index = ayahs.indexOfFirst { it.ayahNumber == ayahNum }
                if (index != -1) {
                    // Adjust index for Bismillah item if present
                    val targetIndex = if (surahNumber != 9 && surahNumber != 1) index + 1 else index
                    listState.animateScrollToItem(targetIndex)
                }
            }
        }
    }
    
    // Scroll to initial ayah when screen loads (from search/bookmarks/etc)
    LaunchedEffect(ayahs, ayahNumber, readingMode) {
        if (ayahs.isNotEmpty() && ayahNumber > 1) {
            println("AlQuranPlusAI: Scrolling to initial ayah $ayahNumber")
            val targetAyah = ayahs.find { it.ayahNumber == ayahNumber }
            targetAyah?.let { ayah ->
                if (readingMode == com.alquranplusai.domain.models.ReadingMode.CONTINUOUS) {
                    val index = ayahs.indexOf(ayah)
                    if (index != -1) {
                        // Adjust index for Bismillah item if present
                        val targetIndex = if (surahNumber != 9 && surahNumber != 1) index + 1 else index
                        listState.scrollToItem(targetIndex)
                        println("AlQuranPlusAI: Scrolled to ayah at index $targetIndex")
                    }
                }
            }
        }
    }
    
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showTafsirSheet by remember { mutableStateOf(false) }
    
    // Tafsir Data
    val currentTafsirTexts by tafsirViewModel.currentTafsirTexts.collectAsState()
    val downloadedTafsirs by tafsirViewModel.downloadedTafsirs.collectAsState()
    
    // Reload Tafsir when downloaded Tafsirs change (e.g., after download completes)
    LaunchedEffect(downloadedTafsirs.size) {
        if (downloadedTafsirs.isNotEmpty() && showTafsirSheet) {
            val targetAyah = activeAyahNumber ?: 1
            tafsirViewModel.loadTafsirForAyah(surahNumber, targetAyah)
        }
    }

    // Tafsir Sheet
    if (showTafsirSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTafsirSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Box(modifier = Modifier.fillMaxHeight(0.6f).fillMaxWidth().padding(horizontal = 16.dp)) {
                if (currentTafsirTexts.isEmpty()) {
                     Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                     Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                         Text(
                             "No Tafsir Available",
                             style = MaterialTheme.typography.titleMedium,
                             fontWeight = FontWeight.Bold
                         )
                         Text(
                             "Download a Tafsir to view commentary",
                             style = MaterialTheme.typography.bodyMedium,
                             color = MaterialTheme.colorScheme.onSurfaceVariant,
                             textAlign = TextAlign.Center
                         )
                         Spacer(modifier = Modifier.height(8.dp))
                         Button(onClick = { 
                             showTafsirSheet = false // Close sheet before navigating
                             onNavigateToTafsirSelection() 
                         }) {
                             Text("Download Tafsir")
                         }
                     }
                    }
                } else {
                    LazyColumn {
                        items(currentTafsirTexts) { text ->
                             com.alquranplusai.android.ui.components.quran.TafsirView(
                                 tafsirTexts = listOf(text),
                                 initiallyExpanded = true
                             )
                        }
                    }
                }
            }
        }
    }
    
    if (showSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Reading Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                
                Text("Arabic Font Size: $fontSize", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = fontSize.toFloat(),
                    onValueChange = { viewModel.setFontSize(it.toInt()) },
                    valueRange = 14f..48f,
                    steps = 16
                )
                 // Temporary: Use buttons inside sheet or update VM to support value setting
                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceAround,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     IconButton(onClick = { viewModel.decreaseFontSize() }) {
                         Text("A-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                     }
                     Text("$fontSize sp", style = MaterialTheme.typography.bodyLarge)
                     IconButton(onClick = { viewModel.increaseFontSize() }) {
                         Text("A+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                     }
                 }
                 
                 Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    
    // Reciter Selection Bottom Sheet
    if (showReciterSheet) {
        val reciters by audioViewModel.reciters.collectAsState()
        val selectedReciter by audioViewModel.selectedReciter.collectAsState()
        
        ModalBottomSheet(
            onDismissRequest = { showReciterSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Reciter",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn {
                    items(reciters) { reciter ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    audioViewModel.selectReciter(reciter)
                                    showReciterSheet = false
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedReciter?.id == reciter.id) 
                                    MaterialTheme.colorScheme.primaryContainer 
                                else 
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reciter.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = reciter.nameArabic,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (selectedReciter?.id == reciter.id) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = "Surah $surahNumber",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Reciter selection button
                    IconButton(onClick = { showReciterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Select Reciter"
                        )
                    }
                    
                    IconButton(onClick = { 
                        // If ayahs are not loaded yet, we can still attempt to play the surah
                        audioViewModel.togglePlayPause(surahNumber)
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }
                    // Tafsir Action
                    IconButton(onClick = { 
                        val targetAyah = activeAyahNumber ?: 1
                        tafsirViewModel.loadTafsirForAyah(surahNumber, targetAyah)
                        showTafsirSheet = true
                    }) {
                        Icon(Icons.Default.Description, contentDescription = "Tafsir")
                    }
                    IconButton(onClick = { showSettingsSheet = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            // Show persistent audio player controls at bottom
            com.alquranplusai.android.ui.components.audio.BottomAudioPlayerBar(
                onOpenFullPlayer = onNavigateToAudioPlayer
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (ayahs.isEmpty()) {
                     Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                         Text("No Ayahs found or failed to load.")
                     }
                } else {
                    if (readingMode == com.alquranplusai.domain.models.ReadingMode.PAGE_BY_PAGE) {
                        // Page View Implementation
                        val pages = remember(ayahs) { ayahs.groupBy { it.pageNumber }.toList().sortedBy { it.first } }
                        val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { pages.size })
                        
                        // Scroll to the page containing the initial ayah  
                        LaunchedEffect(ayahNumber, pages) {
                            if (ayahNumber > 1) {
                                val targetAyah = ayahs.find { it.ayahNumber == ayahNumber }
                                targetAyah?.let { ayah ->
                                    val pageIndex = pages.indexOfFirst { (_, pageAyahs) ->
                                        pageAyahs.any { it.ayahNumber == ayah.ayahNumber }
                                    }
                                    if (pageIndex != -1) {
                                        pagerState.scrollToPage(pageIndex)
                                        println("AlQuranPlusAI: Scrolled to page $pageIndex for ayah $ayahNumber")
                                    }
                                }
                            }
                        }
                        
                        androidx.compose.foundation.pager.HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { pageIndex ->
                            val (pageNumber, pageAyahs) = pages[pageIndex]
                            
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                item { 
                                     Text(
                                         "Page $pageNumber", 
                                         modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), 
                                         textAlign = TextAlign.Center,
                                         style = MaterialTheme.typography.bodySmall,
                                         color = MaterialTheme.colorScheme.outline
                                     )
                                }
                                items(pageAyahs) { ayah ->
                                    val isSurahPlayingNow = isPlaying && activeSurahNumber == surahNumber
                                    val isBookmarked = bookmarksMap.containsKey("$surahNumber:${ayah.ayahNumber}")
                                    val ayahTafsir = tafsirByAyah[ayah.ayahNumber] ?: emptyList()

                                    AyahItem(
                                        ayah = ayah,
                                        fontSize = fontSize,
                                        isBookmarked = isBookmarked,
                                        onToggleBookmark = { viewModel.toggleBookmark(surahNumber, ayah.ayahNumber) },
                                        activeWordPosition = activeWordPosition,
                                        activeAyahNumber = activeAyahNumber,
                                        isSurahPlaying = isSurahPlayingNow,
                                        tafsirTexts = ayahTafsir,
                                        isWordByWordEnabled = isWordByWordEnabled
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                }
                            }
                        }
                    } else {
                        // Continuous View
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            // Bismillah for all Surahs except 9 (At-Tawbah)
                            if (surahNumber != 9) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontFamily = androidx.compose.ui.text.font.FontFamily.Default, // Ideally stick to an Arabic font
                                                fontWeight = FontWeight.Bold
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            items(ayahs) { ayah ->
                                val isSurahPlayingNow = isPlaying && activeSurahNumber == surahNumber
                                if (isSurahPlayingNow && ayah.ayahNumber == activeAyahNumber) {
                                    println("AlQuranPlusAI: Rendering Active Ayah ${ayah.ayahNumber} with Word $activeWordPosition")
                                }
                                val isBookmarked = bookmarksMap.containsKey("$surahNumber:${ayah.ayahNumber}")
                                val ayahTafsir = tafsirByAyah[ayah.ayahNumber] ?: emptyList()
                                
                                AyahItem(
                                    ayah = ayah, 
                                    fontSize = fontSize,
                                    isBookmarked = isBookmarked,
                                    onToggleBookmark = { viewModel.toggleBookmark(surahNumber, ayah.ayahNumber) },
                                    activeWordPosition = activeWordPosition,
                                    activeAyahNumber = activeAyahNumber,
                                    isSurahPlaying = isSurahPlayingNow,
                                    tafsirTexts = ayahTafsir,
                                    isWordByWordEnabled = isWordByWordEnabled
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
                
                // Show info if playing but no sync
                if (isPlaying && activeWordPosition == null && activeAyahNumber != null) {
                    // Small delay to ensure we haven't just skipped a pause
                    LaunchedEffect(Unit) {
                        println("AlQuranPlusAI: Notice - No word timings active for this reciter")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AyahItem(
    ayah: Ayah,
    fontSize: Int,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    translationsMap: Map<String, com.alquranplusai.domain.models.Translation> = emptyMap(),
    activeWordPosition: Int? = null,
    activeAyahNumber: Int? = null,
    isSurahPlaying: Boolean = false,
    tafsirTexts: List<TafsirText> = emptyList(),
    isWordByWordEnabled: Boolean = true
) {
    val isAyahPlaying = isSurahPlaying && activeAyahNumber != null && ayah.ayahNumber == activeAyahNumber

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isAyahPlaying) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        if (isWordByWordEnabled) {
            // Arabic Text with Word-by-Word Highlighting
            val wordsToDisplay = if (ayah.words.isNotEmpty()) {
                ayah.words 
            } else {
                // Fallback: Split text into words if structured word data is missing
                ayah.text.split(" ").mapIndexed { index, text ->
                    com.alquranplusai.domain.models.Word(
                        id = index.toLong(),
                        ayahId = ayah.id,
                        position = index + 1,
                        text = text,
                        textUthmani = text,
                        textSimple = text,
                        translation = null,
                        transliteration = null,
                        root = null,
                        lemma = null,
                        grammar = null,
                        occurrenceCount = 0,
                        audioUrl = null
                    )
                }
            }
    
            CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Display words
                    wordsToDisplay.forEach { word ->
                        val isHighlighted = isAyahPlaying && activeWordPosition != null && word.position == activeWordPosition
                        
                        Surface(
                            color = if (isHighlighted) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = word.text,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontSize = fontSize.sp,
                                    lineHeight = (fontSize * 1.8).sp,
                                    color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Simple Text View (No word breakdown)
             Text(
                 text = ayah.text,
                 style = MaterialTheme.typography.headlineMedium.copy(
                     fontSize = fontSize.sp,
                     lineHeight = (fontSize * 1.6).sp,
                     color = MaterialTheme.colorScheme.onSurface,
                     textAlign = TextAlign.End
                 ),
                 modifier = Modifier.fillMaxWidth()
             )
        }
        
        
        // Translations
        if (ayah.translations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            ayah.translations.forEach { translation ->
                // Translation header with translator name
                val translatorName = translationsMap[translation.translationId]?. let {
                    "${it.languageCode} - ${it.name}"
                } ?: translation.translationId
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = translatorName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    Text(
                        text = translation.text.replace(Regex("<[^>]*>"), ""), // Basic HTML tag removal
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = (fontSize * 0.9).sp,
                            lineHeight = (fontSize * 1.4).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        // Tafsir (Commentary) - Inline under ayah
        if (tafsirTexts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            
            tafsirTexts.forEach { tafsirText ->
                TafsirCard(
                    tafsirText = tafsirText,
                    fontSize = fontSize
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Ayah Number Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onToggleBookmark) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (isBookmarked) "Remove Bookmark" else "Bookmark",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ayah.ayahNumber.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun TafsirCard(
    tafsirText: TafsirText,
    fontSize: Int
) {
    var expanded by remember(tafsirText.id) { mutableStateOf(true) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tafsirText.tafsirId,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFFF6F00),
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tafsirText.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (fontSize * 0.85).sp,
                        lineHeight = (fontSize * 1.3).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
