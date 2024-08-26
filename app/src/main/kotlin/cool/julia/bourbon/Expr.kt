package cool.julia.bourbon

abstract class Expr {
    interface Visitor<R> {
        fun visit(expr: Binary): R
        fun visit(expr: Grouping): R
        fun visit(expr: Literal): R
        fun visit(expr: Unary): R
    }

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    data class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    data class Literal(val value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    data class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}
