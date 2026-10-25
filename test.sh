#!/bin/bash

set -e

if [[ -n $1 ]] ; then
    impl=$1
else
    read -r -p "Choose implementation to test (r/j): " impl
fi

mkdir -p Temp

if [[ "$impl" = "j" ]] ; then
    javac Transpiler/Ruskell.java -d .
    java Ruskell saverun Ruskell/program1.rhs Temp/program1.hs
    java Ruskell saverun Ruskell/snake.rhs Temp/snake.hs
elif [[ "$impl" = "r" ]] ; then
    rustc Transpiler/ruskell.rs -o ruskell
    ./ruskell saverun Ruskell/program1.rhs Temp/program1.hs
    ./ruskell saverun Ruskell/snake.rhs Temp/snake.rhs
else
    echo "Incorrect parameter!";
    echo "Valid parameters: 'r' for Rust, 'j' for Java";
    exit;
fi
