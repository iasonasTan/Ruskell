#!/usr/bin/env bash
set -e
source ./variables.sh

# Generate output directories
mkdir -p "$OUT_DIR" "$BUILD_DIR"

# Compile code
cd "$SRC_DIR"
javac -d "../$OUT_DIR" "$MAIN_CLASS_JAVAC"
cd ..

# Generate jar and copy libs
jar -cfe "$MAIN_JAR_NAME" "$MAIN_CLASS_JAR" -C "$OUT_DIR" . -C "$RESOURCES_DIR" . "$LICENSE_PATH"
cp lib/* "$BUILD_DIR"
