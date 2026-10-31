#!/usr/bin/env bash
set -e
source ./variables.sh

echo "Building app..."

# Generate output directories
mkdir -p "$OUT_DIR" "$BUILD_DIR"

# Compile code
javac --module-path "lib:src" -d "$OUT_DIR" $(find ./src/ -type f -name *.java)

# Generate jar and copy libs
jar -cfe "$MAIN_JAR_NAME" "$MAIN_CLASS_JAR" -C "$OUT_DIR" . -C "$RESOURCES_DIR" . "$LICENSE_PATH"
cp lib/* "$BUILD_DIR"
