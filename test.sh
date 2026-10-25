#!/bin/bash
set -e

mkdir -p Temp
rustc Transpiler/ruskell.rs -o ruskell
./ruskell saverun Ruskell/program1.rhs Temp/program1.hs
./ruskell saverun Ruskell/snake.rhs Temp/snake.rhs
