# Ruskell
Ruskell is just Haskell but in Russian, without symbols or anything, just letters.
There are two implementations. One with Java, and another with Rust.
**Java Implementation is not longer supported.**

## How to use the appication (Java/Rust versions):
To use an app you simply execute the command.
The first argument should be `save`, `run` or `saverun` depending on what you want to do.
The second argument should be the _.rhs_ (ruskell) file path.
The third option is the path to the output _.hs_ file. It should exist, but if you chose mode `run` without `save` you don't have to enter a valid path.

**Cheat Sheet**
```bash
./ruskell save ./rhs/ruskell.rhs ./out/haskell.hs    # Save output in haskell.hs without running it.
./ruskell run  ./rhs/ruskell.rhs .                   # Run ruskell.rhs without saving it. (Third argument will be ignored)
./ruskell saverun ./rhs/ruskell.rhs ./out/haskell.hs # Save output in haskell.hs and run it.

```

**Haskell Cheat Sheet**
```bash
ghc code.hs -o out.bin # Compile and save as out.bin
runghc code.hs         # Run without saving
```
