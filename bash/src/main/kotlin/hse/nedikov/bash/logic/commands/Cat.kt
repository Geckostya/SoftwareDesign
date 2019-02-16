package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.EnvironmentCommand
import java.io.*
import java.lang.Exception

/**
 * cat command which prints files entries to the output stream
 */
class Cat(private val arguments: ArrayList<String>, override val env: Environment) : EnvironmentCommand(env) {
  /**
   * Prints input to the output if has no arguments and prints entries of files from arguments otherwise
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    if (arguments.isNotEmpty()) return execute(output)
    input.forEachLine { output.write("$it\n") }
  }

  /**
   * Prints entries of files from arguments otherwise
   */
  override fun execute(output: PipedWriter) {
    for (arg in arguments) {
      try {
        FileReader(env.getCanonicalPath(arg)).forEachLine { output.write(it + System.lineSeparator()) }
      } catch (e: Exception) {
        output.write("cat: ${e.message}")
      }
      output.write("\n")
    }
  }

}