# AlQuranPlusAI 🕌

A modern, AI-powered Quran application built with Kotlin Multiplatform, featuring offline-first architecture, beautiful Material 3 UI, and advanced learning features.

## ✨ Features

- 📖 **Complete Quran Text** - All 114 Surahs with word-by-word analysis
- 🎧 **Audio Recitation** - Multiple reciters with word-level timing
- 🌍 **Translations** - Multiple languages with tafsir support
- 🔖 **Smart Bookmarks** - Organize with folders and tags
- 🎯 **Gamified Learning** - Quizzes, achievements, and daily challenges
- 🔍 **Advanced Search** - Text and AI-powered semantic search
- 📊 **Analytics** - Track your reading progress and habits
- 🎨 **Beautiful UI** - Material 3 design with dark mode
- 📱 **Offline First** - Works without internet connection

## 🏗️ Architecture

Built with **Clean Architecture** principles:

```
┌─────────────────────────────────────┐
│         Presentation (UI)            │
│      Jetpack Compose + Material 3   │
├─────────────────────────────────────┤
│          Domain Layer                │
│    Models + Repository Interfaces    │
├─────────────────────────────────────┤
│           Data Layer                 │
│  SQLDelight + Ktor + Repositories    │
└─────────────────────────────────────┘
```

### Technology Stack

- **Kotlin Multiplatform** - Shared business logic
- **Jetpack Compose** - Modern declarative UI
- **Material 3** - Latest design system
- **SQLDelight** - Type-safe database
- **Ktor Client** - HTTP networking
- **Koin** - Dependency injection
- **Flow** - Reactive programming

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34

### Building

```bash
# Clone the repository
git clone https://github.com/yourusername/AlQuranPlusAI.git
cd AlQuranPlusAI

# Build the project
./gradlew assembleDebug

# Run tests
./gradlew test

# Install on device
./gradlew installDebug
```

See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for detailed build instructions.

## 📁 Project Structure

```
AlQuranPlusAI/
├── shared/              # Kotlin Multiplatform shared code
│   ├── domain/         # Business logic & models
│   ├── data/           # Repositories & data sources
│   │   ├── database/   # SQLDelight schemas
│   │   ├── network/    # API services
│   │   └── mappers/    # Data mappers
│   └── di/             # Dependency injection
├── androidApp/         # Android application
│   ├── ui/            # Compose UI
│   │   ├── screens/   # App screens
│   │   ├── components/# Reusable components
│   │   └── theme/     # Material 3 theme
│   ├── navigation/    # Navigation setup
│   └── di/            # Android DI
└── docs/              # Documentation
```

## 📊 Current Status

**Completion**: 53% ✅

### ✅ Complete
- Shared module (domain, data, network)
- Android foundation
- 9 core screens (Home, Surah List, Reading, Audio, Bookmarks, Quiz, Profile, Search, Settings)
- UI components library
- Build system

### 🚧 In Progress
- Additional screens
- Data integration
- Audio subsystem

See [PROJECT_PROGRESS.md](docs/PROJECT_PROGRESS.md) for detailed progress.

## 🎨 Design

Follows Material 3 design guidelines with custom color scheme:

- **Primary**: Deep Teal (#006064)
- **Secondary**: Purple (#5E35B1)
- **Accent**: Gold (#FFB300)

See [DESIGN_SPEC.md](docs/DESIGN_SPEC.md) for complete design specifications.

## 🤝 Contributing

Contributions are welcome! Please read our contributing guidelines before submitting PRs.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Quran text from [Tanzil Project](https://tanzil.net)
- Audio recitations from [EveryAyah.com](https://everyayah.com)
- Translations from various sources
- Built with ❤️ using Kotlin Multiplatform

## 📧 Contact

Project Link: [https://github.com/yourusername/AlQuranPlusAI](https://github.com/yourusername/AlQuranPlusAI)

---

**Made with ❤️ for the Muslim community**
