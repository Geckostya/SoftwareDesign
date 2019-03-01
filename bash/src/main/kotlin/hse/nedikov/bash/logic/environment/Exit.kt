package hse.nedikov.bash.logic.environment

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.*

/**
 * Class for command which closes the interpreter
 */
class Exit(override val env: Environment) : Command(env) {
  /**
   * Stops the interpreter
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }

  /**
   * Stops the interpreter
   */
  override fun execute(output: PipedWriter) {
    env.stopInterpreter()
  }
}