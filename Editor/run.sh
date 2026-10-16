#!/usr/bin/env bash
set -e
source ./variables.sh

# $1 can be the path

if [[ -z $1 ]] ; then
  java -jar "$MAIN_JAR_NAME"
else
  java -jar "$MAIN_JAR_NAME" "$1"
fi