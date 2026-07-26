#!/bin/bash

set -e

if [[ -n $1 ]] ; then
    impl=$1
else
    read -r -p "Choose implementation to test (r/j): " impl
fi

if [[ "$impl" = "j" ]] ; then
    mkdir -p Temp
    javac Transpiler/Ruskell.java -d .
    java Ruskell saverun Ruskell/program1.rhs Temp/app.hs
elif [[ "$impl" = "r" ]] ; then
    mkdir -p Temp
    rustc Transpiler/ruskell.rs
    ./ruskell Ruskell/program1.rhs Temp/app.hs
    runghc Temp/app.hs
else
    echo "Incorrect parameter!";
    echo "Valid parameters: 'r' for Rust, 'j' for Java";
    exit;
fi