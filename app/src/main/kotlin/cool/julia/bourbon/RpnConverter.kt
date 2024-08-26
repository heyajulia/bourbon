package cool.julia.bourbon

class RpnConverter : Expr.Visitor<String> {
    fun convert(expr: Expr) = expr.accept(this)

    override fun visit(expr: Expr.Binary) = "${convert(expr.left)} ${convert(expr.right)} ${expr.operator.lexeme}"

    override fun visit(expr: Expr.Grouping) = convert(expr.expression)

    override fun visit(expr: Expr.Literal): String {
        val value = expr.value ?: throw IllegalArgumentException("Value can't be null.")

        if (value !is Int && value !is Double) throw IllegalArgumentException("Value must be an Int or a Double, got ${expr.value::class.simpleName}.")

        return value.toString()
    }

    override fun visit(expr: Expr.Unary): String {
        val op = when (expr.operator.type) {
            TokenType.MINUS -> "NEGATE"
            TokenType.BANG -> "INVERT"
            else -> throw IllegalArgumentException("Invalid operator '${expr.operator.lexeme}'.")
        }

        return "${convert(expr.right)} $op"
    }
}
