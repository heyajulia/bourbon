package cool.julia.bourbon

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.system.exitProcess

object Lox {
    private var hadError = false

    @JvmStatic
    fun main(args: Array<String>) = when (args.size) {
        0 -> runPrompt()
        1 -> runFile(args[0])
        else -> {
            println("Usage: bbn [script]")
            exitProcess(64)
        }
    }

    private fun runFile(path: String) {
        val source = File(path).readText()
        run(source)

        if (hadError) exitProcess(65)
    }

    private fun runPrompt() {
        val input = InputStreamReader(System.`in`)
        val reader = BufferedReader(input)

        while (true) {
            print("> ")

            val line = reader.readLine() ?: break
            run(line)
            hadError = false
        }
    }

    private fun run(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.scan()

        tokens.forEach(::println)
    }

    fun error(line: Int, message: String) = report(line, "", message)

    private fun report(line: Int, where: String, message: String) {
        System.err.println("[line $line] Error$where: $message")
        hadError = true
    }
}
