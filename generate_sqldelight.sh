#!/bin/bash

echo "🔧 Triggering SQLDelight Code Generation..."

# Clean previous builds
echo "  - Cleaning previous builds..."
./gradlew clean > /dev/null 2>&1

# Generate SQLDelight code
echo "  - Generating SQLDelight database code..."
./gradlew :shared:generateCommonMainAlQuranDatabaseInterface

# Check if generation was successful
if [ $? -eq 0 ]; then
    echo "✅ SQLDelight code generated successfully!"
    
    # List generated files
    echo ""
    echo "📁 Generated files:"
    find shared/build/generated/sqldelight -name "*.kt" 2>/dev/null | head -20
    
    echo ""
    echo "🔨 Now attempting compilation..."
    ./gradlew :shared:compileDebugKotlinAndroid
    
    if [ $? -eq 0 ]; then
        echo "✅ Compilation successful!"
    else
        echo "⚠️  Compilation has errors (expected - we'll fix them next)"
    fi
else
    echo "❌ SQLDelight generation failed"
    exit 1
fi
