#!/usr/bin/env python3
"""
Script to add experimental API opt-in annotations to files using experimental Compose APIs.
"""

import os
import re

BASE_DIR = "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/ui/components"

# Files that need FlowRow/FlowColumn opt-in
FLOW_LAYOUT_FILES = [
    "analytics/StreakCalendar.kt",
    "bookmarks/CategoryPicker.kt",
    "bookmarks/TagsInput.kt",
    "search/PopularSearches.kt",
    "search/SearchFilters.kt",
]

def add_opt_in(filepath):
    """Add @OptIn annotation to a file."""
    if not os.path.exists(filepath):
        print(f"⚠ File not found: {filepath}")
        return False
    
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Check if opt-in already exists
    if '@OptIn(ExperimentalLayoutApi::class)' in content or '@OptIn(ExperimentalFoundationApi::class)' in content:
        print(f"✓ Already has opt-in: {filepath}")
        return True
    
    lines = content.split('\n')
    
    # Find the package line
    package_idx = -1
    for i, line in enumerate(lines):
        if line.startswith('package '):
            package_idx = i
            break
    
    if package_idx == -1:
        print(f"⚠ No package declaration in {filepath}")
        return False
    
    # Find where to insert import (after package, before other imports)
    insert_idx = package_idx + 1
    while insert_idx < len(lines) and lines[insert_idx].strip() == '':
        insert_idx += 1
    
    # Add import for ExperimentalLayoutApi
    if 'import androidx.compose.foundation.layout.ExperimentalLayoutApi' not in content:
        lines.insert(insert_idx, 'import androidx.compose.foundation.layout.ExperimentalLayoutApi')
        insert_idx += 1
    
    # Find the @Composable function and add @OptIn before it
    for i in range(len(lines)):
        if '@Composable' in lines[i]:
            # Check if @OptIn is already there
            if i > 0 and '@OptIn' in lines[i-1]:
                break
            # Insert @OptIn before @Composable
            lines.insert(i, '@OptIn(ExperimentalLayoutApi::class)')
            break
    
    # Write back
    with open(filepath, 'w') as f:
        f.write('\n'.join(lines))
    
    print(f"✓ Added opt-in to {filepath}")
    return True

def main():
    print("Adding experimental API opt-in annotations...")
    fixed_count = 0
    
    for relative_path in FLOW_LAYOUT_FILES:
        filepath = os.path.join(BASE_DIR, relative_path)
        if add_opt_in(filepath):
            fixed_count += 1
    
    print(f"\nDone! Added opt-in to {fixed_count}/{len(FLOW_LAYOUT_FILES)} files.")

if __name__ == "__main__":
    main()
