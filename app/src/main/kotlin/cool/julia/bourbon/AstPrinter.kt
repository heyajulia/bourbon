package cool.julia.bourbon

class AstPrinter : Expr.Visitor<String> {
    fun print(expr: Expr) = expr.accept(this)

    private fun parenthesize(name: String, vararg exprs: Expr) =
        buildString {
            append('(')
            append(name)

            exprs.forEach {
                append(' ')
                append(print(it))
            }

            append(')')
        }


    override fun visit(expr: Expr.Binary) = parenthesize(expr.operator.lexeme, expr.left, expr.right)

    override fun visit(expr: Expr.Grouping) = parenthesize("group", expr.expression)

    override fun visit(expr: Expr.Literal) = expr.value?.toString() ?: "nil"

    override fun visit(expr: Expr.Unary) = parenthesize(expr.operator.lexeme, expr.right)
}
