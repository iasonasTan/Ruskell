# Ruskell Editor

## What is Ruskell Editor
This is a AWT/Swing Ruskell editor made with Java.
This editor offers keywords highlighting for now.

## About Project
If you're using terminal, execute script normally.
If you're using an IDE, you should add a configuration that executes:
```bash
./build.sh && ./run.sh
```
or create a script that does this (add it to gitignore if you create it)
example
```
#!/bin/bash
set -e
./build.sh
./run.sh
```

The project is copying Keywords/keywords to JAR on build.
It is expected for the app not to work when it is not a JAR.

Add your script to **.gitignore** by executing `echo 'scriptname.sh' >> .gitignore`.
