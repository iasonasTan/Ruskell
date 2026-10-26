#!/usr/bin/env bash
set -e
source ./variables.sh

# $1 can be the path

cd build

if [[ -z $1 ]] ; then
  ./run.app.sh
else
  ./run.app.sh $1
fi
