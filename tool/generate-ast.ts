import Handlebars from "handlebars";
import dedent from "dedent";

Handlebars.registerHelper("lower", (s: string) => s.toLowerCase());

// deno-lint-ignore ban-types
type KotlinType = "Any?" | "Expr" | "Token" | (string & {});

interface Type {
  name: string;
  fields: Record<string, KotlinType>;
}

const template = Handlebars.compile(
  dedent`
    package cool.julia.bourbon

    abstract class {{baseName}} {
        interface Visitor<R> {
            {{#each types}}
            fun visit({{lower ../baseName}}: {{name}}): R
            {{/each}}
        }

        {{#each types}}
        data class {{name}}(
            {{~#each fields~}}
                val {{@key}}: {{this}}{{#unless @last}}, {{/unless}}
            {{~/each~}}
        ) : {{../baseName}}() {
            override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
        }
        {{#unless @last}}{{"\n"}}{{/unless}}
        {{/each}}

        abstract fun <R> accept(visitor: Visitor<R>): R
    }\n`
);

async function defineAst(baseName: string, types: Type[]) {
  const code = template({ baseName, types })
    .replace(/^ {4}$/gm, "")
    .replace(/\n{3}/, "\n\n");

  await Deno.writeTextFile(`app/src/main/kotlin/cool/julia/bourbon/${baseName}.kt`, code);
}

await defineAst("Expr", [
  {
    name: "Binary",
    fields: {
      left: "Expr",
      operator: "Token",
      right: "Expr",
    },
  },
  { name: "Grouping", fields: { expression: "Expr" } },
  { name: "Literal", fields: { value: "Any?" } },
  { name: "Unary", fields: { operator: "Token", right: "Expr" } },
]);
