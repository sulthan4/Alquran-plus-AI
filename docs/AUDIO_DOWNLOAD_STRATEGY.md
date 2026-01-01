# 🎵 AlQuranPlusAI - Audio Download Strategy

## 📋 Overview

This document defines the comprehensive audio download and playback strategy for AlQuranPlusAI, balancing user experience, storage efficiency, and network usage.

---

## 🎯 Audio Download Modes

### 1. **Streaming Mode** (Default) ⭐ RECOMMENDED

**When:** User plays audio without downloading  
**How:** Stream directly from API URLs  
**Storage:** Minimal (cache only)  
**Network:** Required during playback  

**User Experience:**
- ✅ Instant playback (no wait time)
- ✅ No storage used
- ✅ Always latest version
- ⚠️ Requires internet connection
- ⚠️ Uses data during playback

**Implementation:**
```kotlin
class AudioPlayerViewModel {
    fun playAyah(surahId: Int, ayahId: Int, reciterId: Int) {
        viewModelScope.launch {
            // Get audio URL from API
            val audioUrl = audioRepository.getAudioUrl(surahId, ayahId, reciterId)
            
            // Stream directly (ExoPlayer handles caching)
            exoPlayer.setMediaItem(MediaItem.fromUri(audioUrl))
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }
}
```

**Cache Strategy:**
- ExoPlayer automatically caches recently played audio
- Cache size: 100-200 MB (configurable)
- LRU (Least Recently Used) eviction
- Cleared when storage is low

---

### 2. **On-Demand Download** (User Initiated)

**When:** User explicitly requests download  
**How:** Download and save to local storage  
**Storage:** User-controlled  
**Network:** One-time download  

**User Experience:**
- ✅ Offline playback
- ✅ No data usage after download
- ✅ Faster playback (local file)
- ⚠️ Uses device storage
- ⚠️ Initial download time
- ⚠️ Manual update needed

**Download Options:**

#### Option A: Download Single Surah
```kotlin
fun downloadSurah(surahId: Int, reciterId: Int) {
    viewModelScope.launch {
        downloadManager.downloadSurah(
            surahId = surahId,
            reciterId = reciterId,
            onProgress = { progress -> 
                _downloadProgress.value = progress
            },
            onComplete = { 
                showMessage("Surah downloaded successfully")
            },
            onError = { error ->
                showError("Download failed: ${error.message}")
            }
        )
    }
}
```

#### Option B: Download Multiple Surahs
```kotlin
fun downloadSurahs(surahIds: List<Int>, reciterId: Int) {
    viewModelScope.launch {
        downloadManager.downloadMultipleSurahs(
            surahIds = surahIds,
            reciterId = reciterId,
            onProgress = { current, total, progress ->
                _downloadProgress.value = "Downloading $current/$total ($progress%)"
            }
        )
    }
}
```

#### Option C: Download Complete Quran
```kotlin
fun downloadCompleteQuran(reciterId: Int) {
    viewModelScope.launch {
        // Show confirmation dialog first (large download)
        showConfirmationDialog(
            title = "Download Complete Quran",
            message = "This will download all 114 surahs (~500MB). Continue?",
            onConfirm = {
                downloadManager.downloadCompleteQuran(
                    reciterId = reciterId,
                    onProgress = { progress ->
                        _downloadProgress.value = progress
                    }
                )
            }
        )
    }
}
```

---

### 3. **Smart Download** (Intelligent Prefetch)

**When:** Based on user behavior and patterns  
**How:** Automatically download likely-to-be-played audio  
**Storage:** Configurable limit  
**Network:** Background, WiFi-only  

**Smart Download Triggers:**

#### A. Frequently Read Surahs
```kotlin
class SmartDownloadManager {
    suspend fun analyzeAndDownload() {
        // Get user's most-read surahs
        val frequentSurahs = analyticsRepository.getMostReadSurahs(limit = 10)
        
        // Download if not already downloaded
        frequentSurahs.forEach { surah ->
            if (!isDownloaded(surah.id, currentReciter)) {
                downloadInBackground(surah.id, currentReciter)
            }
        }
    }
}
```

#### B. Next Surah Prediction
```kotlin
fun predictAndPrefetch(currentSurahId: Int) {
    viewModelScope.launch {
        // User likely to read next surah
        val nextSurahId = currentSurahId + 1
        if (nextSurahId <= 114 && !isDownloaded(nextSurahId)) {
            prefetchSurah(nextSurahId, currentReciter)
        }
    }
}
```

#### C. Daily Routine
```kotlin
fun scheduleRoutineDownloads() {
    // Download commonly recited surahs
    val routineSurahs = listOf(
        1,   // Al-Fatihah
        18,  // Al-Kahf (Friday)
        36,  // Ya-Sin
        67,  // Al-Mulk
        78,  // An-Naba
        112, // Al-Ikhlas
        113, // Al-Falaq
        114  // An-Nas
    )
    
    downloadManager.downloadSurahs(routineSurahs, currentReciter)
}
```

---

## 📊 Download Priority System

### Priority Levels

**P0 - Critical (Immediate)**
- Currently playing audio
- Next ayah in sequence
- User-requested download

**P1 - High (Within 5 minutes)**
- Frequently read surahs
- Bookmarked ayahs
- Next surah in reading order

**P2 - Medium (Within 1 hour)**
- Surahs in current Juz
- Playlist items
- Daily routine surahs

**P3 - Low (WiFi only, background)**
- Complete Quran download
- Alternative reciters
- Rarely accessed content

---

## 💾 Storage Management

### Storage Allocation

```kotlin
object AudioStorageConfig {
    // Maximum storage for audio files
    const val MAX_AUDIO_STORAGE_MB = 2000 // 2 GB
    
    // Cache for streaming
    const val STREAMING_CACHE_MB = 200 // 200 MB
    
    // Reserved for system
    const val RESERVED_STORAGE_MB = 500 // 500 MB
    
    fun getAvailableStorage(): Long {
        val totalSpace = Environment.getDataDirectory().totalSpace
        val freeSpace = Environment.getDataDirectory().freeSpace
        return freeSpace - (RESERVED_STORAGE_MB * 1024 * 1024)
    }
}
```

### Storage Cleanup Strategy

```kotlin
class AudioStorageManager {
    
    suspend fun cleanupIfNeeded() {
        val currentUsage = getAudioStorageUsage()
        val maxAllowed = AudioStorageConfig.MAX_AUDIO_STORAGE_MB * 1024 * 1024
        
        if (currentUsage > maxAllowed) {
            // Remove least recently played audio
            val lruAudio = audioRepository.getLeastRecentlyPlayed()
            lruAudio.forEach { audio ->
                deleteAudioFile(audio.id)
                if (getAudioStorageUsage() < maxAllowed * 0.8) {
                    break // Stop when under 80% threshold
                }
            }
        }
    }
    
    suspend fun deleteUnusedAudio(daysUnused: Int = 30) {
        val cutoffDate = Date().time - (daysUnused * 24 * 60 * 60 * 1000)
        val unusedAudio = audioRepository.getAudioNotPlayedSince(cutoffDate)
        
        unusedAudio.forEach { audio ->
            deleteAudioFile(audio.id)
        }
    }
}
```

---

## 🌐 Network Strategy

### WiFi vs Mobile Data

```kotlin
class NetworkAwareDownloadManager {
    
    fun canDownload(downloadSize: Long): Boolean {
        val networkType = getNetworkType()
        val userPreference = settingsRepository.getDownloadPreference()
        
        return when (userPreference) {
            DownloadPreference.WIFI_ONLY -> networkType == NetworkType.WIFI
            DownloadPreference.WIFI_AND_MOBILE -> true
            DownloadPreference.WIFI_OR_SMALL_FILES -> {
                networkType == NetworkType.WIFI || 
                downloadSize < 5 * 1024 * 1024 // 5 MB
            }
            DownloadPreference.NEVER -> false
        }
    }
    
    suspend fun downloadWithNetworkCheck(
        surahId: Int,
        reciterId: Int
    ) {
        val audioInfo = audioRepository.getAudioInfo(surahId, reciterId)
        
        if (!canDownload(audioInfo.size)) {
            showMessage("Download requires WiFi connection")
            return
        }
        
        downloadAudio(surahId, reciterId)
    }
}
```

### Download Queue Management

```kotlin
class DownloadQueue {
    private val queue = PriorityQueue<DownloadTask>(
        compareBy { it.priority }
    )
    
    fun enqueue(task: DownloadTask) {
        queue.add(task)
        processQueue()
    }
    
    private suspend fun processQueue() {
        // Maximum 3 concurrent downloads
        val activeDownloads = queue.filter { it.status == Status.DOWNLOADING }
        
        if (activeDownloads.size < 3) {
            val nextTask = queue.poll()
            nextTask?.let { download(it) }
        }
    }
}

data class DownloadTask(
    val surahId: Int,
    val reciterId: Int,
    val priority: Priority,
    val status: Status
)
```

---

## 🎮 User Controls

### Settings Options

```kotlin
data class AudioSettings(
    // Download preferences
    val downloadPreference: DownloadPreference = DownloadPreference.WIFI_ONLY,
    val autoDownloadFavorites: Boolean = false,
    val maxStorageUsage: Int = 2000, // MB
    
    // Playback preferences
    val defaultPlaybackMode: PlaybackMode = PlaybackMode.STREAMING,
    val audioQuality: AudioQuality = AudioQuality.MEDIUM,
    
    // Cache settings
    val streamingCacheSize: Int = 200, // MB
    val clearCacheOnExit: Boolean = false
)

enum class DownloadPreference {
    WIFI_ONLY,
    WIFI_AND_MOBILE,
    WIFI_OR_SMALL_FILES,
    NEVER
}

enum class PlaybackMode {
    STREAMING,      // Always stream
    OFFLINE_FIRST,  // Use downloaded if available
    DOWNLOAD_FIRST  // Download before playing
}

enum class AudioQuality {
    LOW,    // 64 kbps
    MEDIUM, // 128 kbps
    HIGH    // 192 kbps
}
```

### UI Components

```kotlin
@Composable
fun AudioDownloadControls(
    surah: Surah,
    reciter: Reciter,
    viewModel: AudioViewModel
) {
    val downloadState by viewModel.getDownloadState(surah.id, reciter.id)
        .collectAsState()
    
    when (downloadState) {
        is DownloadState.NotDownloaded -> {
            IconButton(onClick = { 
                viewModel.downloadSurah(surah.id, reciter.id) 
            }) {
                Icon(Icons.Default.Download, "Download")
            }
        }
        is DownloadState.Downloading -> {
            CircularProgressIndicator(
                progress = downloadState.progress
            )
        }
        is DownloadState.Downloaded -> {
            IconButton(onClick = { 
                viewModel.deleteSurah(surah.id, reciter.id) 
            }) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
        is DownloadState.Failed -> {
            IconButton(onClick = { 
                viewModel.retryDownload(surah.id, reciter.id) 
            }) {
                Icon(Icons.Default.Refresh, "Retry")
            }
        }
    }
}
```

---

## 📱 Download Manager Implementation

```kotlin
class AudioDownloadManager(
    private val audioRepository: AudioRepository,
    private val storageManager: AudioStorageManager,
    private val networkManager: NetworkAwareDownloadManager
) {
    
    private val downloadQueue = DownloadQueue()
    private val _downloadStates = MutableStateFlow<Map<String, DownloadState>>(emptyMap())
    val downloadStates: StateFlow<Map<String, DownloadState>> = _downloadStates.asStateFlow()
    
    suspend fun downloadSurah(
        surahId: Int,
        reciterId: Int,
        priority: Priority = Priority.MEDIUM
    ) {
        val key = "$surahId-$reciterId"
        
        // Check if already downloaded
        if (isDownloaded(surahId, reciterId)) {
            return
        }
        
        // Check storage
        if (!storageManager.hasEnoughSpace(estimatedSize)) {
            storageManager.cleanupIfNeeded()
        }
        
        // Check network
        if (!networkManager.canDownload(estimatedSize)) {
            throw NetworkException("Download requires WiFi")
        }
        
        // Add to queue
        downloadQueue.enqueue(
            DownloadTask(surahId, reciterId, priority, Status.QUEUED)
        )
        
        // Update state
        _downloadStates.value = _downloadStates.value + (key to DownloadState.Queued)
    }
    
    private suspend fun download(task: DownloadTask) {
        val key = "${task.surahId}-${task.reciterId}"
        
        try {
            _downloadStates.value = _downloadStates.value + 
                (key to DownloadState.Downloading(0f))
            
            // Get audio URL
            val audioUrl = audioRepository.getAudioUrl(
                task.surahId, 
                task.reciterId
            )
            
            // Download file
            val file = downloadFile(
                url = audioUrl,
                destination = getAudioFilePath(task.surahId, task.reciterId),
                onProgress = { progress ->
                    _downloadStates.value = _downloadStates.value + 
                        (key to DownloadState.Downloading(progress))
                }
            )
            
            // Save to database
            audioRepository.markAsDownloaded(
                surahId = task.surahId,
                reciterId = task.reciterId,
                filePath = file.absolutePath,
                fileSize = file.length()
            )
            
            _downloadStates.value = _downloadStates.value + 
                (key to DownloadState.Downloaded)
                
        } catch (e: Exception) {
            _downloadStates.value = _downloadStates.value + 
                (key to DownloadState.Failed(e.message ?: "Unknown error"))
        }
    }
}
```

---

## 🎯 Recommended Strategy for AlQuranPlusAI

### Default Behavior

1. **Streaming First** (No downloads by default)
   - Instant playback
   - No storage concerns
   - ExoPlayer handles caching

2. **User-Initiated Downloads**
   - Clear download button on each surah
   - Batch download options
   - Download progress indicators

3. **Smart Prefetch** (Optional, WiFi-only)
   - Download frequently read surahs
   - Download next surah in sequence
   - Download bookmarked ayahs

### User Settings

```
Settings > Audio > Downloads
├─ Download Preference: WiFi Only ▼
├─ Auto-download favorites: OFF
├─ Maximum storage: 2 GB
├─ Audio quality: Medium (128kbps)
└─ Clear cache on exit: OFF

Settings > Audio > Playback
├─ Playback mode: Streaming ▼
└─ Streaming cache: 200 MB
```

---

## ✅ Summary

**Answer to your question:** 

**YES, audio should be downloaded only when user requests!**

**Default Strategy:**
- ✅ **Stream by default** (instant playback, no storage)
- ✅ **Download on user request** (offline access)
- ✅ **Smart prefetch optional** (WiFi-only, user-controlled)

**Benefits:**
- No forced downloads
- User controls storage
- Instant playback
- Offline option available
- Network-aware
- Storage-efficient

**Implementation Priority:**
1. Streaming mode (Phase 1)
2. On-demand download (Phase 2)
3. Smart prefetch (Phase 3)

