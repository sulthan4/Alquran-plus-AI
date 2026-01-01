#!/bin/bash

# Configuration
MASTER_ICON="/Users/mohamed/.gemini/antigravity/brain/701411b5-f40a-4ac6-b97e-30210802251b/app_launcher_icon_1766167788807.png"
RES_DIR="androidApp/src/main/res"

# Verify master icon exists
if [ ! -f "$MASTER_ICON" ]; then
    echo "Error: Master icon not found at $MASTER_ICON"
    exit 1
fi

echo "Generating Android Icons from $MASTER_ICON..."

# Function to resize and copy
generate_icon() {
    local density=$1
    local size=$2
    local output_dir="$RES_DIR/mipmap-$density"
    
    mkdir -p "$output_dir"
    
    echo "  - Generating $density (${size}x${size})..."
    sips -z $size $size "$MASTER_ICON" --out "$output_dir/ic_launcher.png" > /dev/null
    
    # Also generate rounded version (just using same icon for now, usually rounded is different)
    sips -z $size $size "$MASTER_ICON" --out "$output_dir/ic_launcher_round.png" > /dev/null
}

# Generate all densities
generate_icon "mdpi" 48
generate_icon "hdpi" 72
generate_icon "xhdpi" 96
generate_icon "xxhdpi" 144
generate_icon "xxxhdpi" 192

echo "✅ App Icons generated successfully!"
