#!/usr/bin/env bash
set -e

# Create temp and output directory if it doesn't exist
mkdir -p Temp
mkdir -p out
mkdir -p ../Editor/build

# Compile Transpiler and copy resources
rustc src/ruskell.rs -o out/ruskell

# Copy ruskell to java program destination
cp out/ruskell ../Editor/build/
