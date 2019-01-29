package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.lang.Exception

class Cat(override val arguments: ArrayList<String>) : Command(arguments) {
  override fun execute(input: PipedReader, output: PipedWriter) {
    if (arguments.isNotEmpty()) return execute(output)
    input.forEachLine { output.write("$it\n") }
  }

  override fun execute(output: PipedWriter) {
    for (arg in arguments) {
      try {
        FileReader(arg).forEachLine { output.write(arg) }
      } catch (e: Exception) {
        output.write("cat: ${e.message}")
      }
      output.write("\n")
    }
  }

}