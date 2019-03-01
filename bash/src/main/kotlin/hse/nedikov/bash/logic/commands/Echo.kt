package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.*
import java.util.*


/**
 * echo command which prints arguments to the output
 */
class Echo(private val arguments: ArrayList<String>, override val env: Environment = Environment()) : Command(env) {
  /**
   * Prints arguments which are joined with spaces to the output
   */
  override fun execute(output: PipedWriter) {
    val sj = StringJoiner(" ", "", "\n")
    arguments.forEach { sj.add(it) }
    output.write(sj.toString())
    output.close()
  }

  /**
   * Prints arguments which are joined with spaces to the output
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }
}