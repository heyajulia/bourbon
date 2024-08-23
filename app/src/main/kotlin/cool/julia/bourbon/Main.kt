package cool.julia.bourbon

class Main {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    println(Main().greeting)
}
