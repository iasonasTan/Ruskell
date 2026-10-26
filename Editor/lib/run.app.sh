#!/usr/bin/env bash

if [[ -n $1 ]] ; then
    java --module-path "." --add-modules JeLib.io,JeLib.core -jar Ruskell-Editor.jar $1
else
    java --module-path "." --add-modules JeLib.io,JeLib.core -jar Ruskell-Editor.jar
fi
