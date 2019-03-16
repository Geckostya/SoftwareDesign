package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.lang.Exception

/**
 * cat command which prints files entries to the output stream
 */
class Cat(private val arguments: ArrayList<String>) : Command() {
  /**
   * Prints input to the output if has no arguments and prints entries of files from arguments otherwise
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    if (arguments.isNotEmpty()) return execute(output)
    input.forEachLine { output.write(it + System.lineSeparator()) }
  }

  /**
   * Prints entries of files from arguments otherwise
   */
  override fun execute(output: PipedWriter) {
    for (arg in arguments) {
      try {
        FileReader(arg).forEachLine { output.write(it + System.lineSeparator()) }
      } catch (e: Exception) {
        throw Exception("cat: ${e.message}")
      }
      output.write(System.lineSeparator())
    }
  }

}