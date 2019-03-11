package hse.nedikov.bash

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    println("welcome!")
    val errorHandler: (Exception) -> Unit = { println("error: ${it.message}") }
    val controller = Controller(errorHandler)
    while (controller.isWorking()) {
      try {
        controller.runCommandLine(readLine() ?: "") { println(it) }
      } catch (e: java.lang.Exception) {
        errorHandler(e)
      }
    }
  }
}