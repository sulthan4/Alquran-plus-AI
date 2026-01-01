# AlQuranPlusAI - UI/UX Design Specification

**Design Vision**: Modern, beautiful, spiritually uplifting Islamic app that makes Quran study accessible and engaging.

---

## 🎨 Design Principles

1. **Beauty with Purpose** - Every element serves both aesthetic and functional goals
2. **Islamic Aesthetics** - Geometric patterns, calligraphy, traditional colors
3. **Modern & Clean** - Material Design 3, generous white space, smooth animations
4. **Accessibility First** - High contrast, scalable fonts, clear hierarchy
5. **Emotional Connection** - Design that inspires and motivates daily engagement

---

## 🏠 Home Screen Design (LOCKED)

### Layout Structure

```
┌─────────────────────────────────────┐
│  Header (Gradient: Teal → Purple)  │
│  👤 AlQuran Plus AI            🔔  │
├─────────────────────────────────────┤
│                                     │
│  ┌───────────────────────────────┐ │
│  │  Continue Reading             │ │
│  │  Surah Al-Baqarah, Ayah 255  │ │
│  │  ﷽ [Arabic text preview]     │ │
│  │  ▓▓▓▓▓▓▓▓▓░░░ 255/286       │ │
│  │                          🔖   │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌─────┐  ┌─────┐  ┌─────┐        │
│  │ 🔥  │  │ 📖  │  │ ⏱️  │        │
│  │  7  │  │ 12  │  │ 25  │        │
│  │Days │  │Ayahs│  │ min │        │
│  └─────┘  └─────┘  └─────┘        │
│                                     │
│  ┌──────────┐  ┌──────────┐       │
│  │ 📚       │  │ 🎵       │       │
│  │ Browse   │  │ Audio    │       │
│  │ Quran    │  │ Player   │       │
│  └──────────┘  └──────────┘       │
│  ┌──────────┐  ┌──────────┐       │
│  │ 🔖       │  │ 🏆       │       │
│  │Bookmarks │  │  Daily   │       │
│  │          │  │  Quiz    │       │
│  └──────────┘  └──────────┘       │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ ⭐ Today's Challenge          │ │
│  │ Complete 5 verses from...     │ │
│  │ [Start Challenge →]           │ │
│  └───────────────────────────────┘ │
│                                     │
│                              🔍    │
├─────────────────────────────────────┤
│  🏠  📖  🎵  🔖  👤              │
└─────────────────────────────────────┘
```

### Color Palette (LOCKED)

**Primary Colors**:
- Deep Teal: `#006064` - Primary brand color
- Purple: `#5E35B1` - Secondary accent
- Gold: `#FFB300` - Highlights and achievements

**Gradients**:
- Header: Teal (#006064) → Purple (#5E35B1)
- Continue Reading Card: Teal (#00838F) → Deep Teal (#006064)
- Browse Quran: Teal gradient
- Audio Player: Purple gradient
- Bookmarks: Orange (#FF6F00) gradient
- Daily Quiz: Green (#2E7D32) gradient

**Neutral Colors**:
- Background: `#FAFAFA` (light mode), `#121212` (dark mode)
- Card Background: `#FFFFFF` with elevation
- Text Primary: `#212121` (light), `#FFFFFF` (dark)
- Text Secondary: `#757575` (light), `#B0B0B0` (dark)

**Semantic Colors**:
- Success/Complete: `#2E7D32` (Green)
- Warning: `#F57C00` (Orange)
- Error: `#C62828` (Red)
- Info: `#0277BD` (Blue)

**Supporting Feature Colors**:
- Browse Quran: Teal `#00838F`
- Audio Player: Purple `#5E35B1`
- Bookmarks: Orange `#FF6F00`
- Daily Quiz: Green `#2E7D32`
- Search: Blue `#0277BD`
- Analytics: Indigo `#3949AB`

**Color Philosophy**:

The color scheme was carefully chosen to:
- ✅ **Evoke spirituality** - Teal and purple are calming, meditative colors that encourage reflection
- ✅ **Feel premium** - Rich, deep tones convey quality and excellence
- ✅ **Be accessible** - High contrast ratios (WCAG AA compliant) for readability
- ✅ **Honor tradition** - Gold accents reference Islamic art and illuminated manuscripts
- ✅ **Stand out** - Unique palette differentiates from other Quran apps in the market
- ✅ **Create hierarchy** - Clear visual distinction between primary, secondary, and accent colors

**Primary Theme Identity**: **Deep Teal (#006064)** - This is THE signature color that users will associate with AlQuran Plus AI across all touchpoints.


### Typography (LOCKED)

**Arabic Text**:
- Font: Uthmanic Hafs (primary), Amiri Quran (alternative)
- Sizes: 
  - Hero (Continue Reading): 24sp
  - Reading view: 28-32sp (user adjustable)
  - Word-by-word: 20sp

**UI Text**:
- Font: Roboto (Android standard)
- Heading 1: 24sp, Bold
- Heading 2: 20sp, Medium
- Body: 16sp, Regular
- Caption: 14sp, Regular

### Component Specifications

#### 1. Continue Reading Card
```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    elevation = 4.dp,
    shape = RoundedCornerShape(16.dp),
    backgroundColor = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00838F), Color(0xFF006064))
    )
) {
    Column(padding = 16.dp) {
        Text("Continue Reading", style = MaterialTheme.typography.h6, color = White)
        Spacer(8.dp)
        Text("Surah Al-Baqarah, Ayah 255", style = MaterialTheme.typography.subtitle1)
        Spacer(8.dp)
        Text("[Arabic text]", fontFamily = UthamanicHafs, fontSize = 24.sp)
        Spacer(12.dp)
        LinearProgressIndicator(progress = 0.89f, color = Gold)
        Text("Verse 255 of 286", fontSize = 12.sp)
    }
}
```

#### 2. Quick Stats Cards
```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
    StatCard(icon = "🔥", value = "7", label = "Days", weight = 1f)
    StatCard(icon = "📖", value = "12", label = "Ayahs", weight = 1f)
    StatCard(icon = "⏱️", value = "25", label = "min", weight = 1f)
}

@Composable
fun StatCard(icon: String, value: String, label: String, weight: Float) {
    Card(
        modifier = Modifier.weight(weight),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            padding = 12.dp
        ) {
            Text(icon, fontSize = 24.sp)
            Text(value, fontSize = 20.sp, fontWeight = Bold)
            Text(label, fontSize = 12.sp, color = Gray)
        }
    }
}
```

#### 3. Feature Grid Cards
```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(2), spacing = 12.dp) {
    item { FeatureCard("Browse Quran", icon, TealGradient) }
    item { FeatureCard("Audio Player", icon, PurpleGradient) }
    item { FeatureCard("Bookmarks", icon, OrangeGradient) }
    item { FeatureCard("Daily Quiz", icon, GreenGradient) }
}

@Composable
fun FeatureCard(title: String, icon: ImageVector, gradient: Brush) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { /* navigate */ },
        elevation = 3.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = gradient
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, size = 48.dp, tint = White)
            Spacer(8.dp)
            Text(title, fontSize = 16.sp, fontWeight = Medium, color = White)
        }
    }
}
```

### Animations (LOCKED)

**Page Transitions**:
- Fade + Slide (300ms, EaseInOut)

**Card Interactions**:
- Ripple effect on tap
- Subtle scale (0.98x) on press
- Elevation change (2dp → 8dp) on hover

**Progress Indicators**:
- Smooth animated progress bars
- Shimmer effect while loading

**Micro-interactions**:
- Bookmark icon: Heart beat animation
- Streak flame: Flicker animation
- Achievement unlock: Confetti + scale

### Spacing System (LOCKED)

```kotlin
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}
```

### Elevation System (LOCKED)

```kotlin
object Elevation {
    val none = 0.dp
    val low = 2.dp
    val medium = 4.dp
    val high = 8.dp
    val highest = 16.dp
}
```

---

## 📱 Other Key Screens (To Be Designed)

### Reading Screen
- Full-screen Quran text
- Word-by-word highlighting
- Translation below each Ayah
- Swipe navigation
- Bottom sheet for settings

### Audio Player
- Album art style reciter image
- Waveform visualization
- Word-by-word synchronized highlighting
- Playback controls
- Playlist queue

### Quiz Screen
- Question card with timer
- Multiple choice options
- Progress indicator
- Explanation on answer
- Score celebration

### Bookmark Screen
- Folder hierarchy
- Card-based bookmark list
- Swipe actions (edit, delete)
- Search and filter
- Tag chips

---

## 🎯 Design Commitments

✅ **This design is LOCKED and will be implemented exactly as specified**  
✅ All colors, spacing, typography follow this specification  
✅ No deviations without explicit approval  
✅ Every screen will maintain this aesthetic  
✅ Code will match these Compose specifications  

---

**This design honors the Quran and serves the users with beauty and functionality.**

**Last Updated**: December 18, 2025, 11:56 PM IST
