package hse.nedikov.bash

import hse.nedikov.bash.exceptions.ParseException

fun main(args: Array<String>) {
  val env = Environment()
  while (env.isWorking()) {
    try {
      val line = readLine()
      val reader = lex(Parser(line ?: "", env.variables).parse(), env).invoke()
      reader.forEachLine { println(it) }
    } catch (e: Exception) {
      println("error: ${e.message}")
    }
  }
}