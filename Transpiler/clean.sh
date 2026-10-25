#!/usr/bin/env bash

remove() {
    echo "Removing $1..."
    rm -rf $1
}

remove out
remove .temp
remove Temp

echo "Done!"
