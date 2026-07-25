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

    java Ruskell Ruskell/program1.rhs Temp/app.hs
elif [[ "$impl" = "r" ]] ; then
    rustc Transpiler/transpiler.rs

    ./transpiler Ruskell/program1.rhs Temp/app.hs
else
    echo "Incorrect parameter!";
    exit;
fi

runghc Temp/app.hs