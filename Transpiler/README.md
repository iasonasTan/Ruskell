# Transpiler

This is the core of the project.

It's a Rust program that converts Ruskell code to runnable (using runghc) haskell code.

## About project

### About building

There are some pre-made bash scripts.

* `clean.sh` cleans all the copied files and removes the executables

* `test.sh` Cleans, compiles and runs the executable, converting and running two ruskell programs

### About code

The project is just one big rust file.

File structure:
```rust
mod transpier {} // Contains methods that convert code from ruskell to haskell
mod runner {}    // Contains methdos that run haskell files
mod saver {}     // Contains methdos that save output
```
