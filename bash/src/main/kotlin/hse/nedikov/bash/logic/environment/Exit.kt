package hse.nedikov.bash.logic.environment

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.EnvironmentCommand
import java.io.*

class Exit(override val env: Environment) : EnvironmentCommand(env) {
  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }

  override fun execute(output: PipedWriter) {
    env.stopProgram()
  }
}