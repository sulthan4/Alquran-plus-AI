# AlQuranPlusAI - Build Instructions

## 🚀 Quick Start

### Prerequisites
- JDK 17 or higher
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34

### Building the Project

1. **Clone the repository** (if not already done)

2. **Open in Android Studio**
   ```bash
   # Open the project folder in Android Studio
   ```

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Or run: `./gradlew --refresh-dependencies`

4. **Build the project**
   ```bash
   # Build debug APK
   ./gradlew assembleDebug
   
   # Build release APK
   ./gradlew assembleRelease
   
   # Run tests
   ./gradlew test
   ```

### Running the App

1. **Using Android Studio**
   - Click the "Run" button (green play icon)
   - Select your device/emulator

2. **Using Command Line**
   ```bash
   # Install on connected device
   ./gradlew installDebug
   
   # Run the app
   adb shell am start -n com.alquranplusai.android/.MainActivity
   ```

## 📁 Project Structure

```
AlQuranPlusAI/
├── shared/              # Kotlin Multiplatform shared code
│   ├── domain/         # Business logic & models
│   ├── data/           # Repositories & data sources
│   └── di/             # Dependency injection
├── androidApp/         # Android application
│   ├── ui/            # Compose UI screens
│   ├── navigation/    # Navigation setup
│   └── theme/         # Material 3 theme
└── docs/              # Documentation
```

## 🛠️ Development

### Code Style
- Follow Kotlin coding conventions
- Use ktlint for formatting
- Run `./gradlew ktlintFormat` before committing

### Testing
```bash
# Run all tests
./gradlew test

# Run Android instrumented tests
./gradlew connectedAndroidTest
```

## 📊 Current Status

- **Completion**: 53%
- **Files**: 138
- **Lines of Code**: 19,100+

### ✅ Complete
- Shared module (domain, data, network)
- Android foundation
- 9 core screens
- UI components library

### 🚧 In Progress
- Additional screens
- Data integration
- Audio subsystem

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Run tests
4. Submit a pull request

## 📝 License

[Your License Here]

## 🙏 Acknowledgments

Built with:
- Kotlin Multiplatform
- Jetpack Compose
- Material 3
- SQLDelight
- Ktor
- Koin
