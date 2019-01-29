package hse.nedikov.bash

fun main(args: Array<String>) {
  val env = Environment()
  while (env.isWorking()) {
    val line = readLine()
    val reader = lex(Parser(line ?: "", env.variables).parse(), env).invoke()
    reader.forEachLine { println(it) }
  }
}