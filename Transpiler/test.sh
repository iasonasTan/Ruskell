#!/usr/bin/env bash
set -e

# Create temp and output directory if it doesn't exist
mkdir -p Temp
mkdir -p out

# Compile Transpiler and copy resources
rustc src/ruskell.rs -o out/ruskell

# Execute tests with transpiler
./out/ruskell saverun ../Ruskell/program1.rhs Temp/program1.hs
./out/ruskell saverun ../Ruskell/snake.rhs    Temp/snake.hs
