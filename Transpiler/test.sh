#!/usr/bin/env bash
set -e

./build.sh

# Execute tests with transpiler
./out/ruskell saverun ../Ruskell/program1.rhs Temp/program1.hs
./out/ruskell saverun ../Ruskell/snake.rhs    Temp/snake.hs
