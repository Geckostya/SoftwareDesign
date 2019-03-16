package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.util.*


/**
 * echo command which prints arguments to the output
 */
class Echo(private val arguments: ArrayList<String>) : Command() {
  /**
   * Prints arguments which are joined with spaces to the output
   */
  override fun execute(output: PipedWriter) {
    val result = java.lang.String.join(" ", arguments)
    output.write(result.toString())
  }

  /**
   * Prints arguments which are joined with spaces to the output
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }
}