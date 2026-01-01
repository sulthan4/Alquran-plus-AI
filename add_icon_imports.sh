#!/bin/bash

# Script to add Material Icons imports to all UI component files

echo "Adding Material Icons imports to UI components..."

# Define the base directory
BASE_DIR="/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/ui/components"

# Function to add imports to a file if they don't exist
add_icon_imports() {
    local file="$1"
    shift
    local icons=("$@")
    
    # Check if file exists
    if [ ! -f "$file" ]; then
        return
    fi
    
    # Check if Icons import already exists
    if ! grep -q "import androidx.compose.material.icons.Icons" "$file"; then
        # Find the package line
        local package_line=$(grep -n "^package " "$file" | cut -d: -f1)
        if [ -n "$package_line" ]; then
            # Add imports after package and existing imports
            local import_line=$((package_line + 1))
            
            # Build import statements
            local imports="import androidx.compose.material.icons.Icons\n"
            for icon in "${icons[@]}"; do
                imports+="import androidx.compose.material.icons.filled.$icon\n"
            done
            
            # Insert imports
            sed -i "" "${import_line}a\\
$imports
" "$file"
            echo "✓ Added icons to $file"
        fi
    fi
}

# Analytics components
add_icon_imports "$BASE_DIR/analytics/MilestoneCard.kt" "CheckCircle"
add_icon_imports "$BASE_DIR/analytics/TrendChart.kt" "TrendingUp" "TrendingDown"

# Audio components
add_icon_imports "$BASE_DIR/audio/AudioControls.kt" "SkipPrevious" "Pause" "PlayArrow" "SkipNext"
add_icon_imports "$BASE_DIR/audio/DownloadButton.kt" "CheckCircle" "Download"
add_icon_imports "$BASE_DIR/audio/MiniPlayer.kt" "Pause" "PlayArrow"
add_icon_imports "$BASE_DIR/audio/PlayerControls.kt" "Shuffle" "SkipPrevious" "Pause" "PlayArrow" "SkipNext" "Repeat"
add_icon_imports "$BASE_DIR/audio/RepeatModeButton.kt" "Repeat"
add_icon_imports "$BASE_DIR/audio/ShuffleButton.kt" "Shuffle"
add_icon_imports "$BASE_DIR/audio/VolumeSlider.kt" "VolumeDown" "VolumeUp"

# Bookmark components
add_icon_imports "$BASE_DIR/bookmarks/BookmarkFolderCard.kt" "Folder"
add_icon_imports "$BASE_DIR/bookmarks/FolderListItem.kt" "Folder"
add_icon_imports "$BASE_DIR/bookmarks/QuickBookmarkButton.kt" "Bookmark"
add_icon_imports "$BASE_DIR/bookmarks/ReminderCard.kt" "Delete"
add_icon_imports "$BASE_DIR/bookmarks/TagChip.kt" "Close"

# Common components
add_icon_imports "$BASE_DIR/common/BackButton.kt" "ArrowBack"
add_icon_imports "$BASE_DIR/common/SearchBar.kt" "Search"

# Quiz components
add_icon_imports "$BASE_DIR/quiz/AchievementBadge.kt" "Star"
add_icon_imports "$BASE_DIR/quiz/DailyChallengeCard.kt" "CheckCircle"
add_icon_imports "$BASE_DIR/quiz/HintCard.kt" "Lightbulb"

# Quran components
add_icon_imports "$BASE_DIR/quran/ReadingControls.kt" "Bookmark" "Share" "PlayArrow"
add_icon_imports "$BASE_DIR/quran/SajdaIndicator.kt" "Star"

# Search components
add_icon_imports "$BASE_DIR/search/AIInsightsCard.kt" "Lightbulb"
add_icon_imports "$BASE_DIR/search/RecentSearches.kt" "History"
add_icon_imports "$BASE_DIR/search/TrendingSearches.kt" "TrendingUp"
add_icon_imports "$BASE_DIR/search/VoiceIndicator.kt" "Mic"

echo "Done! Material Icons imports added."
