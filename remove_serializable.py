#!/usr/bin/env python3
"""
Script to remove @Serializable annotations from workers and services.
These annotations are causing issues and aren't strictly necessary for the basic functionality.
"""

import os
import re

FILES_TO_FIX = [
    "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/workers/AnalyticsWorker.kt",
    "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/services/BackupService.kt",
    "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/services/RestoreService.kt",
    "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/services/SyncService.kt",
    "/Users/mohamed/.gemini/antigravity/scratch/AlQuranPlusAI/androidApp/src/main/kotlin/com/alquranplusai/android/services/UpdateService.kt",
]

def remove_serializable_annotations(filepath):
    """Remove @Serializable annotations and related imports."""
    if not os.path.exists(filepath):
        print(f"⚠ File not found: {filepath}")
        return False
    
    with open(filepath, 'r') as f:
        content = f.read()
    
    lines = content.split('\n')
    cleaned_lines = []
    
    for line in lines:
        # Skip @Serializable annotations
        if line.strip() == '@Serializable':
            continue
        # Skip kotlinx.serialization imports (but keep json.Json)
        if 'import kotlinx.serialization.Serializable' in line:
            continue
        if 'import kotlinx.serialization.encodeToString' in line:
            continue
        if 'import kotlinx.serialization.decodeFromString' in line:
            continue
        
        cleaned_lines.append(line)
    
    # Write back
    with open(filepath, 'w') as f:
        f.write('\n'.join(cleaned_lines))
    
    print(f"✓ Cleaned {os.path.basename(filepath)}")
    return True

def main():
    print("Removing @Serializable annotations...")
    fixed_count = 0
    
    for filepath in FILES_TO_FIX:
        if remove_serializable_annotations(filepath):
            fixed_count += 1
    
    print(f"\nDone! Cleaned {fixed_count}/{len(FILES_TO_FIX)} files.")

if __name__ == "__main__":
    main()
