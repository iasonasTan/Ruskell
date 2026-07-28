# Haskell Stuff
In this repo, I'm putting things I learn in haskell.

## What is Haskell?
Haskell is a functional programming language, that means:
1. No while, for or any other type of loop.
2. Only function calls and recursion.

## What is Ruskell?
Just haskell, but in russian, without symbols or anything.
There are two implementations. One with java, and another with Rust.

## What this means?
This means that haskell is a very unique language that helps us
understand how maths work and think in a more math-functional way
instead of a procedural, Clang-like way.

## How to use the appication (Java/Rust versions):
To use an app you simply execute the command.
The first argument should be `save`, `run` or `saverun` depending on what you want to do.
The second argument should be the _.rhs_ (ruskell) file path.
The third option is the path to the output _.hs_ file. It should exist, but if you chose mode `run` without `save` you don't have to enter a valid path.

**Cheat Sheet**
```
./ruskell save ./rhs/ruskell.rhs ./out/haskell.hs    # Save output in haskell.hs without running it.
./ruskell run  ./rhs/ruskell.rhs .                   # Run ruskell.rhs without saving it. (Third argument will be ignored)
./ruskell saverun ./rhs/ruskell.rhs ./out/haskell.hs # Save output in haskell.hs and run it.

```

### Notes.
I'm very excited because I did all of these in just 30 mins of learning.
Of course I used an LLM (Google Gemini AI) but I used it as a 'Stackoverflow'
faster alternative and a error-explainer instead of a tool that writes the code for me.
