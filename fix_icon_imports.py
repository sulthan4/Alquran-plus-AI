#!/usr/bin/env python3
"""
Script to fix Material Icons imports in UI component files.
"""

import os
import re

BASE_DIR = "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/ui/components"

# Map of files to their required icons
ICON_MAPPINGS = {
    "analytics/MilestoneCard.kt": ["CheckCircle"],
    "analytics/TrendChart.kt": ["TrendingUp", "TrendingDown"],
    "audio/AudioControls.kt": ["SkipPrevious", "Pause", "PlayArrow", "SkipNext"],
    "audio/DownloadButton.kt": ["CheckCircle", "Download"],
    "audio/MiniPlayer.kt": ["Pause", "PlayArrow"],
    "audio/PlayerControls.kt": ["Shuffle", "SkipPrevious", "Pause", "PlayArrow", "SkipNext", "Repeat"],
    "audio/RepeatModeButton.kt": ["Repeat"],
    "audio/ShuffleButton.kt": ["Shuffle"],
    "audio/VolumeSlider.kt": ["VolumeDown", "VolumeUp"],
    "bookmarks/BookmarkFolderCard.kt": ["Folder"],
    "bookmarks/FolderListItem.kt": ["Folder"],
    "bookmarks/QuickBookmarkButton.kt": ["Bookmark"],
    "bookmarks/ReminderCard.kt": ["Delete"],
    "bookmarks/TagChip.kt": ["Close"],
    "common/BackButton.kt": ["ArrowBack"],
    "common/SearchBar.kt": ["Search"],
    "quiz/AchievementBadge.kt": ["Star"],
    "quiz/DailyChallengeCard.kt": ["CheckCircle"],
    "quiz/HintCard.kt": ["Lightbulb"],
    "quran/ReadingControls.kt": ["Bookmark", "Share", "PlayArrow"],
    "quran/SajdaIndicator.kt": ["Star"],
    "search/AIInsightsCard.kt": ["Lightbulb"],
    "search/RecentSearches.kt": ["History"],
    "search/TrendingSearches.kt": ["TrendingUp"],
    "search/VoiceIndicator.kt": ["Mic"],
}

def fix_file(filepath, icons):
    """Fix imports in a single file."""
    if not os.path.exists(filepath):
        print(f"⚠ File not found: {filepath}")
        return False
    
    with open(filepath, 'r') as f:
        content = f.read()
    
    lines = content.split('\n')
    
    # Find package line
    package_idx = -1
    for i, line in enumerate(lines):
        if line.startswith('package '):
            package_idx = i
            break
    
    if package_idx == -1:
        print(f"⚠ No package declaration in {filepath}")
        return False
    
    # Remove any corrupted icon imports
    cleaned_lines = []
    for line in lines:
        # Skip corrupted import lines
        if 'Iconsnimport' in line or ('import androidx.compose.material.icons' in line and 'n\\n' in line):
            continue
        cleaned_lines.append(line)
    
    # Find where to insert imports (after package, before other imports or first non-import line)
    insert_idx = package_idx + 1
    
    # Skip empty lines after package
    while insert_idx < len(cleaned_lines) and cleaned_lines[insert_idx].strip() == '':
        insert_idx += 1
    
    # Check if Icons import already exists (properly formatted)
    has_icons_import = any('import androidx.compose.material.icons.Icons' in line and 'Iconsnimport' not in line 
                          for line in cleaned_lines)
    
    if not has_icons_import:
        # Create import statements
        import_lines = ['import androidx.compose.material.icons.Icons']
        for icon in sorted(set(icons)):  # Remove duplicates and sort
            import_lines.append(f'import androidx.compose.material.icons.filled.{icon}')
        
        # Insert imports
        for i, import_line in enumerate(import_lines):
            cleaned_lines.insert(insert_idx + i, import_line)
        
        # Write back
        with open(filepath, 'w') as f:
            f.write('\n'.join(cleaned_lines))
        
        print(f"✓ Fixed {filepath}")
        return True
    else:
        # Just clean up corrupted lines
        with open(filepath, 'w') as f:
            f.write('\n'.join(cleaned_lines))
        print(f"✓ Cleaned {filepath}")
        return True

def main():
    print("Fixing Material Icons imports...")
    fixed_count = 0
    
    for relative_path, icons in ICON_MAPPINGS.items():
        filepath = os.path.join(BASE_DIR, relative_path)
        if fix_file(filepath, icons):
            fixed_count += 1
    
    print(f"\nDone! Fixed {fixed_count}/{len(ICON_MAPPINGS)} files.")

if __name__ == "__main__":
    main()
