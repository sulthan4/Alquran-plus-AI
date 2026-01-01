#!/usr/bin/env python3
"""
Script to fix common Android compilation errors
"""
import os
import re
from pathlib import Path

android_app_path = Path("androidApp/src/main/kotlin/com/alquranplusai/android")

# Fix 1: Remove getInstance() singleton patterns from utility classes
utils_path = android_app_path / "utils"
for kt_file in utils_path.glob("*.kt"):
    content = kt_file.read_text()
    
    # Remove getInstance singleton pattern
    pattern = r'    companion object \{\s+@Volatile\s+private var INSTANCE:.*?\n.*?fun getInstance\(context: Context\):.*?\{.*?\}\s+\n'
    content = re.sub(pattern, '    companion object {\n', content, flags=re.DOTALL)
    
    kt_file.write_text(content)
    print(f"Fixed: {kt_file.name}")

# Fix 2: Add missing imports
import_fixes = {
    'kotlinx.serialization.json.Json': ['SyncService.kt', 'BackupService.kt', 'RestoreService.kt'],
    'androidx.work.workDataOf': ['*Worker.kt'],
}

for import_stmt, file_patterns in import_fixes.items():
    for pattern in file_patterns:
        for kt_file in android_app_path.rglob(pattern):
            content = kt_file.read_text()
            if import_stmt not in content and 'import' in content:
                # Add import after package declaration
                content = content.replace(
                    'package ',
                    f'package ',
                    1
                ).replace(
                    '\n\nimport',
                    f'\n\nimport {import_stmt}\nimport',
                    1
                )
                kt_file.write_text(content)
                print(f"Added {import_stmt} to {kt_file.name}")

print("Batch fixes complete!")
