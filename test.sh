#!/bin/bash

set -e

javac Transpiler/Ruskell.java -d .

mkdir -p Temp

java Ruskell \
    Ruskell/program1.rs Temp/app.hs \

ghc Temp/app.hs -o ./app

./app