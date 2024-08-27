# 🥃 Bourbon

**Bourbon** (or **bbn**) is an implementation of a bytecode VM for the Lox programming language. It is based on the book
[Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom.

## 🦆 Run the app

```
./gradlew jar && java -jar app/build/libs/app.jar
```

## 🗺️ Regenerate AST classes

To regenerate the AST classes, install Deno and run the following command:

```bash
deno task generate-ast
```

## 🔧 Regenerate `deno.json`

To regenerate the `deno.json` file after making changes to the [Dhall source file](./deno.dhall), run the following
command:

```bash
deno task generate-deno-json
```

`dhall-to-json` must be installed for this to work.
