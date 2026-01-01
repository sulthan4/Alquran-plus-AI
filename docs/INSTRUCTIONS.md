# AlQuranPlusAI - Implementation Instructions

**Project**: AlQuran Plus AI - Kotlin Multiplatform Quran Application  
**Status**: Foundation Complete (~25%)  
**Next Phase**: Data Layer Completion & Android App Development

---

## ⚠️ CRITICAL: Code Preservation Rule

**When fixing build/compilation errors:**
- **DO NOT remove implementation code**
- **ONLY fix the actual issue** (imports, syntax, type mismatches, missing queries)
- Keep all logic and implementation intact
- If database queries are missing, create the schema/queries - don't stub the code
- Fix by adding what's missing, not by removing what exists

---

## 📖 Table of Contents
1. [Quick Start](#quick-start)
2. [Project Structure](#project-structure)
3. [What's Been Done](#whats-been-done)
4. [Next Steps](#next-steps)
5. [Implementation Guidelines](#implementation-guidelines)
6. [Testing Strategy](#testing-strategy)
7. [Common Patterns](#common-patterns)

---

## 🚀 Quick Start

### Prerequisites
- Android Studio (latest version)
- JDK 17+
- Kotlin 1.9.22+
- Gradle 8.2+

### Current Build Status
The project is **not yet buildable** as we're missing:
- Repository implementations
- Network layer
- Android app module implementation
- DI configuration

### To Continue Development

1. **Complete Shared Module Data Layer**:
   ```
   Priority Order:
   1. Repository implementations (8 files)
   2. Network layer (DTOs + API services)
   3. Data mappers (7 files)
   4. DI modules (8 files)
   ```

2. **Start Android App Module**:
   ```
   Priority Order:
   1. Application class + MainActivity
   2. Navigation setup
   3. Theme system
   4. Core ViewModels (Home, Reading, Audio)
   5. Essential UI components
   6. Key screens (Home, Reading, Audio Player)
   ```

---

## 📁 Project Structure

```
AlQuranPlusAI/
├── gradle/
│   └── libs.versions.toml          ✅ Complete dependency catalog
├── shared/                          ✅ KMP shared module
│   ├── build.gradle.kts            ✅ Configured
│   └── src/
│       ├── commonMain/
│       │   ├── kotlin/com/alquranplusai/
│       │   │   ├── domain/
│       │   │   │   ├── models/      ✅ 9 model files
│       │   │   │   └── repositories/ ✅ 8 interface files
│       │   │   ├── data/
│       │   │   │   ├── database/    ✅ Infrastructure complete
│       │   │   │   ├── network/     ❌ TODO
│       │   │   │   ├── ai/          ❌ TODO
│       │   │   │   ├── audio/       ❌ TODO
│       │   │   │   ├── mappers/     ❌ TODO
│       │   │   │   └── repositories/ ❌ TODO
│       │   │   ├── utils/           ✅ 5 utility files
│       │   │   └── di/              ❌ TODO
│       │   └── sqldelight/com/alquranplusai/database/
│       │       └── *.sq             ✅ 20 schema files
│       └── androidMain/
│           └── kotlin/              ✅ Database driver
└── androidApp/                      ❌ TODO (entire module)
```

**Legend**: ✅ Complete | ⚠️ Partial | ❌ Not Started

---

## ✅ What's Been Done

### 1. Domain Layer (100% Complete)
All domain models and repository interfaces are defined. These represent the business logic contracts.

**Key Files**:
- `domain/models/*.kt` - 9 comprehensive model files
- `domain/repositories/*.kt` - 8 repository interface files

### 2. Database Layer (100% Schema, 50% Infrastructure)
Complete SQLDelight schema with 20+ tables covering all features.

**Key Files**:
- `sqldelight/com/alquranplusai/database/*.sq` - 20 schema files
- `data/database/AlQuranDatabase.kt` - Database wrapper
- `data/database/DatabaseDriverFactory.kt` - Platform driver factory

**Database Features**:
- ✅ Quran text (Surah, Ayah, Word with grammar)
- ✅ Translations & Tafsir
- ✅ Audio & Reciters with word timing
- ✅ Playlists
- ✅ Bookmarks, Folders, Tags, Notes
- ✅ Quizzes, Questions, Sessions, Results
- ✅ User profiles & Statistics
- ✅ Analytics & Reading sessions
- ✅ Achievements & Goals
- ✅ Settings & Preferences

### 3. Utilities (100% Complete)
Essential utility functions and extensions.

**Key Files**:
- `utils/Constants.kt` - App-wide constants
- `utils/Logger.kt` - Logging utility
- `utils/FlowExtensions.kt` - Reactive utilities
- `utils/StringExtensions.kt` - String & Arabic processing
- `utils/DateTimeFormatter.kt` - Date/time formatting

---

## 🎯 Next Steps

### Phase 1: Complete Shared Module Data Layer (High Priority)

#### Step 1.1: Repository Implementations
Create concrete implementations for all 8 repositories in `data/repositories/`:

**Template Pattern**:
```kotlin
class QuranRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val apiService: QuranApiService,
    private val mapper: QuranMapper
) : QuranRepository {
    
    override suspend fun getAllSurahs(): Flow<List<Surah>> = flow {
        // Try local first
        val local = database.surahQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(mapper::toSurah) }
        
        emitAll(local)
        
        // Sync from remote if needed
        // ...
    }
    
    // Implement all interface methods
}
```

**Files to Create**:
1. `QuranRepositoryImpl.kt`
2. `TranslationRepositoryImpl.kt`
3. `AudioRepositoryImpl.kt`
4. `BookmarkRepositoryImpl.kt`
5. `QuizRepositoryImpl.kt`
6. `SearchRepositoryImpl.kt`
7. `AnalyticsRepositoryImpl.kt`
8. `UserRepositoryImpl.kt`

#### Step 1.2: Network Layer
Create DTOs and API services in `data/network/`:

**Structure**:
```
data/network/
├── HttpClientFactory.kt
├── NetworkConfig.kt
├── AuthInterceptor.kt
├── ErrorHandler.kt
├── api/
│   ├── QuranApiService.kt
│   ├── TranslationApiService.kt
│   ├── AudioApiService.kt
│   ├── BookmarkApiService.kt
│   ├── QuizApiService.kt
│   ├── SearchApiService.kt
│   ├── AnalyticsApiService.kt
│   ├── UserApiService.kt
│   └── AuthApiService.kt
└── dto/
    ├── SurahDto.kt
    ├── AyahDto.kt
    ├── WordDto.kt
    ├── TranslationDto.kt
    ├── AudioDto.kt
    ├── ReciterDto.kt
    ├── BookmarkDto.kt
    ├── QuizDto.kt
    ├── QuestionDto.kt
    ├── UserDto.kt
    └── ... (20+ DTO files)
```

**HttpClientFactory Template**:
```kotlin
object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger
                level = LogLevel.INFO
            }
            install(Auth) {
                bearer {
                    loadTokens { /* Load from storage */ }
                }
            }
            defaultRequest {
                url(Constants.API_BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}
```

#### Step 1.3: Data Mappers
Create mappers to convert between DTOs ↔ Entities ↔ Domain Models:

**Files to Create**:
1. `QuranMapper.kt`
2. `TranslationMapper.kt`
3. `AudioMapper.kt`
4. `BookmarkMapper.kt`
5. `QuizMapper.kt`
6. `AnalyticsMapper.kt`
7. `UserMapper.kt`

**Mapper Template**:
```kotlin
class QuranMapper {
    fun toSurah(entity: SurahEntity): Surah {
        return Surah(
            number = entity.number,
            name = entity.name,
            // ... map all fields
        )
    }
    
    fun toEntity(surah: Surah): SurahEntity {
        // Reverse mapping
    }
    
    fun toDto(surah: Surah): SurahDto {
        // DTO mapping
    }
}
```

#### Step 1.4: Dependency Injection
Create Koin modules in `di/`:

**Files to Create**:
1. `AppModule.kt` - Core dependencies
2. `DatabaseModule.kt` - Database & DAOs
3. `NetworkModule.kt` - HTTP client & API services
4. `RepositoryModule.kt` - Repository implementations
5. `AIModule.kt` - AI engines
6. `AudioModule.kt` - Audio player
7. `PreferencesModule.kt` - Settings & storage
8. `UseCaseModule.kt` - Use cases (if needed)

**Module Template**:
```kotlin
val databaseModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { AlQuranDatabaseWrapper(get()) }
}

val repositoryModule = module {
    single<QuranRepository> { 
        QuranRepositoryImpl(get(), get(), get()) 
    }
    // ... other repositories
}
```

---

### Phase 2: Android App Foundation

#### Step 2.1: Create `androidApp/build.gradle.kts`
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.alquranplusai.android"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.alquranplusai"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    
    // ... rest of configuration
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    // ... other dependencies
}
```

#### Step 2.2: Application & MainActivity
```kotlin
// AlQuranApplication.kt
class AlQuranApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlQuranApplication)
            modules(allModules)
        }
    }
}

// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlQuranTheme {
                AppNavGraph()
            }
        }
    }
}
```

#### Step 2.3: Navigation Setup
Create in `navigation/`:
1. `AppNavGraph.kt` - Main navigation graph
2. `NavRoutes.kt` - Route definitions
3. `NavArguments.kt` - Navigation arguments
4. `BottomNavigation.kt` - Bottom nav bar
5. `DeepLinkHandler.kt` - Deep link handling

#### Step 2.4: Theme System
Create in `ui/theme/`:
1. `Theme.kt` - Main theme composable
2. `Color.kt` - Color palette
3. `Typography.kt` - Text styles
4. `ArabicFonts.kt` - Arabic font definitions
5. `Spacing.kt` - Spacing scale
6. `Shapes.kt` - Shape definitions

---

## 📐 Implementation Guidelines

### Coding Standards
1. **Package Naming**: Follow strict package structure as defined
2. **File Naming**: Use descriptive names matching class/object names
3. **Imports**: Organize imports, remove unused
4. **Documentation**: Add KDoc for public APIs
5. **Null Safety**: Use nullable types appropriately

### Repository Pattern
```kotlin
interface Repository {
    // Prefer Flow for reactive data
    suspend fun getData(): Flow<List<Data>>
    
    // Use suspend for one-shot operations
    suspend fun saveData(data: Data)
    
    // Return Resource for loading states
    suspend fun fetchFromNetwork(): Flow<Resource<Data>>
}
```

### ViewModel Pattern
```kotlin
class FeatureViewModel(
    private val repository: Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            repository.getData()
                .catch { e -> _uiState.value = UiState.Error(e.message) }
                .collect { data -> _uiState.value = UiState.Success(data) }
        }
    }
}
```

### Compose UI Pattern
```kotlin
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Success -> SuccessContent(state.data)
        is UiState.Error -> ErrorScreen(state.message)
    }
}
```

---

## 🧪 Testing Strategy

### Unit Tests
- Test ViewModels with fake repositories
- Test repository implementations with in-memory database
- Test mappers for correct conversions
- Test utilities and extensions

### Integration Tests
- Test database queries
- Test API service calls
- Test end-to-end flows

### UI Tests
- Test navigation flows
- Test user interactions
- Test screen states

---

## 🔄 Common Patterns

### Error Handling
```kotlin
try {
    val result = apiService.getData()
    Resource.Success(result)
} catch (e: Exception) {
    Logger.e("Error fetching data", e)
    Resource.Error(e.message ?: "Unknown error")
}
```

### Offline-First
```kotlin
override suspend fun getData(): Flow<Data> = flow {
    // Emit local data first
    emitAll(database.query().asFlow().map { it.toDomain() })
    
    // Fetch from network and update local
    try {
        val remote = apiService.getData()
        database.insert(remote.toEntity())
    } catch (e: Exception) {
        Logger.w("Failed to sync from network", e)
    }
}
```

### State Management
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

---

## 📚 Additional Resources

### Documentation
- See `PROJECT_PROGRESS.md` for detailed completion status
- See `implementation_plan.md` for full technical plan
- See `task.md` for checklist tracking

### External APIs Needed
- Quran text API (e.g., api.quran.com)
- Translation APIs
- Audio file hosting
- User authentication service
- Analytics backend

### Assets Required
- Quran text files (JSON/SQLite)
- Translation files (50+ languages)
- Audio files (30+ reciters)
- TensorFlow Lite models
- Font files (Arabic fonts)
- App icons and images

---

## 🤝 Contributing

When continuing this project:
1. Follow the exact folder structure defined
2. Use the patterns and templates provided
3. Update `PROJECT_PROGRESS.md` as you complete sections
4. Mark items in `task.md` as completed
5. Write tests for new code
6. Document complex logic

---

**Last Updated**: December 18, 2025, 11:36 PM IST  
**Next Session**: Continue with Phase 1 (Repository Implementations)
