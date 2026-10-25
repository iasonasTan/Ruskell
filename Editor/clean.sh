#!/usr/bin/env bash
source ./variables.sh

remove() {
    echo "Removing $1..."
    rm -rf "$1"
}

remove $BUILD_DIR
remove $OUT_DIR
