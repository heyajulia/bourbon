let srcDir = "app/src/main/kotlin/cool/julia/bourbon"

let esm =
      λ(package : Text) →
      λ(version : Text) →
        { mapKey = package
        , mapValue = "https://esm.sh/v135/${package}@${version}"
        }

in  { lock = False
    , imports = [ esm "dedent" "1.5.3", esm "handlebars" "4.7.8" ]
    , tasks =
      { generate-ast =
          "deno run --allow-write=${srcDir}/Expr.kt tool/generate-ast.ts"
      , generate-deno-json =
          "dhall-to-json --explain --file deno.dhall --output deno.json"
      }
    }
