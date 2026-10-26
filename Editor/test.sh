#!/usr/bin/env bash
set -e
source ./variables.sh

./clean.sh
./build.sh
./run.sh ../../Ruskell/snake.rhs
