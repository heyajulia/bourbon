package cool.julia.bourbon

import cool.julia.bourbon.TokenType.*

class Scanner(private val source: String) {
    private val tokens = mutableListOf<Token>()

    private val isAtEnd
        get() = current >= source.length

    private var start = 0
    private var current = 0
    private var line = 1

    fun scan(): List<Token> {
        while (!isAtEnd) {
            start = current
            scanToken()
        }

        tokens.add(Token(EOF, "", null, line))

        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            '/' -> if (match('/')) {
                while (!isAtEnd && peek() != '\n') advance()
            } else {
                addToken(SLASH)
            }

            ' ', '\r', '\t' -> {}
            '\n' -> line++
            '"' -> string()
            else -> when {
                isDigit(c) -> number()
                isAlpha(c) -> identifier()
                else -> Lox.error(line, "Unexpected character.")
            }
        }
    }

    private fun string() {
        while (!isAtEnd && peek() != '"') {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd) {
            Lox.error(line, "Unterminated string.")
            return
        }

        advance()

        val value = source.substring(start + 1, current - 1)

        addToken(STRING, value)
    }

    private fun number() {
        advanceWhile(::isDigit)

        if (peek() == '.' && isDigit(peekNext())) {
            advance()

            advanceWhile(::isDigit)
        }

        addToken(NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        advanceWhile(::isAlphaNumeric)

        val text = source.substring(start, current)
        val type = keywords[text] ?: IDENTIFIER

        addToken(type)
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun peek() = if (isAtEnd) Char.MIN_VALUE else source[current]

    private fun peekNext(): Char {
        if (current + 1 >= source.length) return Char.MIN_VALUE
        return source[current + 1]
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd) return false
        if (source[current] != expected) return false

        current++

        return true
    }

    private fun advance() = source[current++]

    private fun advanceWhile(predicate: (Char) -> Boolean) {
        while (predicate(peek())) advance()
    }

    private companion object {
        private val keywords = mapOf(
            "and" to AND,
            "class" to CLASS,
            "else" to ELSE,
            "false" to FALSE,
            "for" to FOR,
            "fun" to FUN,
            "if" to IF,
            "nil" to NIL,
            "or" to OR,
            "print" to PRINT,
            "return" to RETURN,
            "super" to SUPER,
            "this" to THIS,
            "true" to TRUE,
            "var" to VAR,
            "while" to WHILE
        )

        private fun isAlpha(c: Char) = (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'

        private fun isDigit(c: Char) = c in '0'..'9'

        private fun isAlphaNumeric(c: Char) = isAlpha(c) || isDigit(c)
    }
}