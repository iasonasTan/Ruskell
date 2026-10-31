#!/usr/bin/env bash
set -e
source ./variables.sh

# $1 can be the path

cd build

if [[ -z $1 ]] ; then
  echo "Running application without parameters."
  ./run.app.sh
else
  echo "Running application with parameters."
  ./run.app.sh $1
fi
