package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.*

/**
 * pwd command which prints current working directory
 */
class Pwd(override val env: Environment) : Command(env) {
  /**
   * Prints current working directory to the output
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }


  /**
   * Prints current working directory to the output
   */
  override fun execute(output: PipedWriter) {
    output.write(env.getPath("./") + "\n")
  }

}