package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.EnvironmentCommand
import java.io.*

/**
 * pwd command which prints current working directory
 */
class Pwd(override val env: Environment) : EnvironmentCommand(env) {
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
    output.write(env.getCanonicalPath("./") + System.lineSeparator())
  }

}